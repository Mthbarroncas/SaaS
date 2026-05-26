package com.fraudshield.platform.risk.api.dto;

import com.fraudshield.platform.risk.domain.Decision;
import com.fraudshield.platform.risk.domain.RiskLevel;
import java.util.List;
import java.util.UUID;

public record RiskDecisionResponse(
        UUID assessmentId,
        int score,
        Decision decision,
        RiskLevel riskLevel,
        List<String> reasonCodes,
        boolean reviewRequired
) {
}
