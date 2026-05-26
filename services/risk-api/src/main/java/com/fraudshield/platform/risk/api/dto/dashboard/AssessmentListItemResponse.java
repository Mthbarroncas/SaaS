package com.fraudshield.platform.risk.api.dto.dashboard;

import com.fraudshield.platform.risk.domain.Decision;
import com.fraudshield.platform.risk.domain.RiskLevel;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record AssessmentListItemResponse(
        UUID assessmentId,
        String entityType,
        String entityId,
        String assessmentType,
        int score,
        Decision decision,
        RiskLevel riskLevel,
        List<String> reasonCodes,
        OffsetDateTime createdAt
) {
}
