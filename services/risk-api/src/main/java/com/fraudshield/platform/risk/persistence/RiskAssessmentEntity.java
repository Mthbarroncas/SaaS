package com.fraudshield.platform.risk.persistence;

import com.fraudshield.platform.risk.domain.Decision;
import com.fraudshield.platform.risk.domain.RiskLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "risk_assessments")
public class RiskAssessmentEntity {

    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId;

    @Column(name = "entity_type", nullable = false)
    private String entityType;

    @Column(name = "entity_id", nullable = false)
    private String entityId;

    @Column(name = "assessment_type", nullable = false)
    private String assessmentType;

    @Column(nullable = false)
    private int score;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Decision decision;

    @Column(name = "risk_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    @Column(name = "reason_codes", nullable = false, length = 4000)
    private String reasonCodes;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    public static RiskAssessmentEntity of(
            String tenantId,
            String entityType,
            String entityId,
            String assessmentType,
            int score,
            Decision decision,
            RiskLevel riskLevel,
            List<String> reasonCodes
    ) {
        RiskAssessmentEntity entity = new RiskAssessmentEntity();
        entity.id = UUID.randomUUID();
        entity.tenantId = tenantId;
        entity.entityType = entityType;
        entity.entityId = entityId;
        entity.assessmentType = assessmentType;
        entity.score = score;
        entity.decision = decision;
        entity.riskLevel = riskLevel;
        entity.reasonCodes = toJsonArray(reasonCodes);
        entity.createdAt = OffsetDateTime.now();
        return entity;
    }

    private static String toJsonArray(List<String> values) {
        return values.stream().map(value -> "\"" + value + "\"").reduce((a, b) -> a + "," + b).map(s -> "[" + s + "]").orElse("[]");
    }

    public UUID getId() {
        return id;
    }

    public String getTenantId() {
        return tenantId;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public int getScore() {
        return score;
    }

    public Decision getDecision() {
        return decision;
    }

    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public String getReasonCodes() {
        return reasonCodes;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
