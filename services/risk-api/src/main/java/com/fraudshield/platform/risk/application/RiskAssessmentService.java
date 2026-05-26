package com.fraudshield.platform.risk.application;

import com.fraudshield.platform.common.tenant.TenantContext;
import com.fraudshield.platform.risk.api.dto.DeviceContext;
import com.fraudshield.platform.risk.api.dto.RiskDecisionResponse;
import com.fraudshield.platform.risk.api.dto.dashboard.AssessmentListItemResponse;
import com.fraudshield.platform.risk.api.dto.dashboard.DashboardSummaryResponse;
import com.fraudshield.platform.risk.api.dto.dashboard.DeviceListItemResponse;
import com.fraudshield.platform.risk.persistence.AlertEntity;
import com.fraudshield.platform.risk.persistence.AlertRepository;
import com.fraudshield.platform.risk.persistence.DeviceEntity;
import com.fraudshield.platform.risk.persistence.DeviceRepository;
import com.fraudshield.platform.risk.persistence.RiskAssessmentEntity;
import com.fraudshield.platform.risk.persistence.RiskAssessmentRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RiskAssessmentService {

    private final RiskAssessmentRepository riskAssessmentRepository;
    private final AlertRepository alertRepository;
    private final DeviceRepository deviceRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final boolean kafkaEnabled;

    public RiskAssessmentService(
            RiskAssessmentRepository riskAssessmentRepository,
            AlertRepository alertRepository,
            DeviceRepository deviceRepository,
            KafkaTemplate<String, Object> kafkaTemplate,
            @Value("${fraudshield.messaging.kafka-enabled:true}") boolean kafkaEnabled) {
        this.riskAssessmentRepository = riskAssessmentRepository;
        this.alertRepository = alertRepository;
        this.deviceRepository = deviceRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaEnabled = kafkaEnabled;
    }

    @Transactional
    public RiskDecisionResponse persistAssessment(
            String entityType,
            String entityId,
            String assessmentType,
            RiskEvaluationResult evaluation,
            DeviceContext device) {
        String tenantId = TenantContext.get() != null ? TenantContext.get() : "default";

        RiskAssessmentEntity entity = RiskAssessmentEntity.of(
                tenantId,
                entityType,
                entityId,
                assessmentType,
                evaluation.score(),
                evaluation.decision(),
                evaluation.riskLevel(),
                evaluation.reasonCodes());
        riskAssessmentRepository.save(entity);

        if (device != null && device.deviceId() != null && !device.deviceId().isBlank()) {
            DeviceEntity deviceEntity = deviceRepository.findByTenantIdAndDeviceId(tenantId, device.deviceId())
                    .orElseGet(() -> DeviceEntity.create(
                            tenantId,
                            device.deviceId(),
                            hash(device.canvasFingerprint() + "|" + device.webglFingerprint() + "|" + device.userAgent()),
                            evaluation.riskLevel().name()));
            deviceEntity.registerSeen(evaluation.riskLevel().name());
            deviceRepository.save(deviceEntity);
        }

        if (evaluation.decision().name().equals("BLOCK") || evaluation.decision().name().equals("REVIEW")) {
            alertRepository.save(AlertEntity.create(
                    tenantId,
                    entity.getId(),
                    evaluation.riskLevel().name(),
                    assessmentType + " flagged for " + evaluation.decision().name()));
        }

        if (kafkaEnabled) {
            kafkaTemplate.send("risk.assessments", tenantId, entity.getId().toString());
        }

        return new RiskDecisionResponse(
                entity.getId(),
                evaluation.score(),
                evaluation.decision(),
                evaluation.riskLevel(),
                evaluation.reasonCodes(),
                evaluation.decision().name().equals("REVIEW"));
    }

    public List<RiskAssessmentEntity> latestAssessments() {
        String tenantId = TenantContext.get() != null ? TenantContext.get() : "default";
        return riskAssessmentRepository.findTop20ByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    public RiskAssessmentEntity findAssessment(String assessmentId) {
        String tenantId = TenantContext.get() != null ? TenantContext.get() : "default";
        return riskAssessmentRepository.findByIdAndTenantId(UUID.fromString(assessmentId), tenantId)
                .orElseThrow(() -> new IllegalArgumentException("Assessment not found"));
    }

    public List<AlertEntity> latestAlerts() {
        String tenantId = TenantContext.get() != null ? TenantContext.get() : "default";
        return alertRepository.findTop20ByTenantIdOrderByCreatedAtDesc(tenantId);
    }

    public List<DeviceListItemResponse> latestDevices() {
        return deviceRepository.findTop50ByTenantIdOrderByLastSeenAtDesc(currentTenant()).stream()
                .map(device -> new DeviceListItemResponse(
                        device.getDeviceId(),
                        device.getTrustLevel(),
                        device.getLastRiskLevel(),
                        device.getSeenCount(),
                        device.isBlocked(),
                        device.getLastSeenAt()))
                .toList();
    }

    public List<AssessmentListItemResponse> latestAssessmentItems() {
        return latestAssessments().stream()
                .map(assessment -> new AssessmentListItemResponse(
                        assessment.getId(),
                        assessment.getEntityType(),
                        assessment.getEntityId(),
                        assessment.getAssessmentType(),
                        assessment.getScore(),
                        assessment.getDecision(),
                        assessment.getRiskLevel(),
                        parseReasonCodes(assessment.getReasonCodes()),
                        assessment.getCreatedAt()))
                .toList();
    }

    public DashboardSummaryResponse dashboardSummary() {
        List<RiskAssessmentEntity> assessments = latestAssessments();
        String tenantId = currentTenant();
        long blocked = assessments.stream().filter(a -> "BLOCK".equals(a.getDecision().name())).count();
        long review = assessments.stream().filter(a -> "REVIEW".equals(a.getDecision().name())).count();
        long approved = assessments.stream().filter(a -> "APPROVE".equals(a.getDecision().name())).count();
        int averageScore = assessments.isEmpty()
                ? 0
                : (int) Math.round(assessments.stream().mapToInt(RiskAssessmentEntity::getScore).average().orElse(0));

        return new DashboardSummaryResponse(
                assessments.size(),
                blocked,
                review,
                approved,
                alertRepository.countByTenantIdAndStatus(tenantId, "OPEN"),
                alertRepository.countByTenantIdAndStatus(tenantId, "ACKNOWLEDGED"),
                deviceRepository.countByTenantId(tenantId),
                deviceRepository.countByTenantIdAndBlockedTrue(tenantId),
                averageScore,
                blocked * 25990L + review * 14990L,
                blocked * 2 + review);
    }

    @Transactional
    public AlertEntity acknowledgeAlert(String alertId) {
        AlertEntity alert = alertRepository.findByIdAndTenantId(UUID.fromString(alertId), currentTenant())
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));
        alert.acknowledge();
        return alertRepository.save(alert);
    }

    private String currentTenant() {
        return TenantContext.get() != null ? TenantContext.get() : "default";
    }

    private List<String> parseReasonCodes(String jsonArray) {
        String cleaned = jsonArray == null ? "" : jsonArray.trim();
        if (cleaned.length() < 2) {
            return List.of();
        }
        String content = cleaned.substring(1, cleaned.length() - 1).trim();
        if (content.isEmpty()) {
            return List.of();
        }

        return List.of(content.split(",")).stream()
                .map(value -> value.replace("\"", "").trim())
                .map(value -> value.replace("_", " ").toLowerCase(Locale.ROOT))
                .map(this::toTitleCase)
                .toList();
    }

    private String toTitleCase(String value) {
        String[] parts = value.split(" ");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].isBlank()) {
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(parts[i].charAt(0))).append(parts[i].substring(1));
        }
        return builder.toString();
    }

    private String hash(String source) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return HexFormat.of().formatHex(digest.digest(source.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("sha-256 not available", ex);
        }
    }
}
