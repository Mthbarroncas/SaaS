package com.fraudshield.platform.risk.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record LoginAnalyzeRequest(
        @NotBlank String customerId,
        boolean unusualCountry,
        boolean simultaneousLogin,
        boolean excessiveAttempts,
        @Valid DeviceContext device
) {
}
