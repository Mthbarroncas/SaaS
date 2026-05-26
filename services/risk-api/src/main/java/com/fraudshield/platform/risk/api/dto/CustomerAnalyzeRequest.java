package com.fraudshield.platform.risk.api.dto;

import jakarta.validation.constraints.NotBlank;

public record CustomerAnalyzeRequest(
        @NotBlank String customerId,
        boolean accountCompromised,
        boolean loyaltyAbuseDetected,
        boolean promotionAbuseDetected,
        int previousChargebacks
) {
}
