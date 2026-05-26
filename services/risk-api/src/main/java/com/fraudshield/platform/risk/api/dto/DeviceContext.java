package com.fraudshield.platform.risk.api.dto;

public record DeviceContext(
        String deviceId,
        String userAgent,
        String browser,
        String operatingSystem,
        String language,
        String timezone,
        String ip,
        String screenResolution,
        String canvasFingerprint,
        String webglFingerprint,
        boolean newDevice,
        boolean vpnDetected,
        boolean proxyDetected,
        boolean torDetected,
        boolean automationDetected
) {
}
