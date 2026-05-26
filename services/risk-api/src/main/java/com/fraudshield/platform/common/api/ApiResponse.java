package com.fraudshield.platform.common.api;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record ApiResponse<T>(
        UUID requestId,
        OffsetDateTime timestamp,
        T data,
        List<String> errors
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(UUID.randomUUID(), OffsetDateTime.now(), data, List.of());
    }
}
