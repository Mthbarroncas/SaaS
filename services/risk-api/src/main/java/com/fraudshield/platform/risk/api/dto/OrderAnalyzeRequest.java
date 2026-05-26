package com.fraudshield.platform.risk.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OrderAnalyzeRequest(
        @NotBlank String orderId,
        @NotBlank String customerId,
        @Min(1) long amountCents,
        @NotBlank String currency,
        boolean recentAccount,
        boolean multipleAccountsDetected,
        boolean couponAbuseDetected,
        boolean unusualLocation,
        @Valid DeviceContext device
) {
}
