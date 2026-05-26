package com.fraudshield.platform.risk.application;

import com.fraudshield.platform.risk.domain.Decision;
import com.fraudshield.platform.risk.domain.RiskLevel;
import java.util.List;

public record RiskEvaluationResult(
        int score,
        Decision decision,
        RiskLevel riskLevel,
        List<String> reasonCodes
) {
}
