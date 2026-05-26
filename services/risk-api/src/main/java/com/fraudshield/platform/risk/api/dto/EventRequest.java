package com.fraudshield.platform.risk.api.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.OffsetDateTime;
import java.util.Map;

public record EventRequest(
        @NotBlank String eventType,
        String customerId,
        String sessionId,
        String deviceId,
        OffsetDateTime occurredAt,
        Map<String, Object> payload
) {
}
