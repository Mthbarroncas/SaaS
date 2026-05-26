package com.fraudshield.platform.risk.api.dto.dashboard;

import java.time.OffsetDateTime;

public record DeviceListItemResponse(
        String deviceId,
        String trustLevel,
        String lastRiskLevel,
        int seenCount,
        boolean blocked,
        OffsetDateTime lastSeenAt
) {
}
