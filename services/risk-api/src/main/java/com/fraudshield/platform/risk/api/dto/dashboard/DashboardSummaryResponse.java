package com.fraudshield.platform.risk.api.dto.dashboard;

public record DashboardSummaryResponse(
        long totalAssessments,
        long blockedAssessments,
        long reviewAssessments,
        long approvedAssessments,
        long openAlerts,
        long acknowledgedAlerts,
        long totalDevices,
        long blockedDevices,
        int averageScore,
        long protectedRevenueCents,
        long preventedChargebacks
) {
}
