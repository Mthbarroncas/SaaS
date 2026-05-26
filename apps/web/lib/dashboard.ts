export type DashboardSummary = {
  totalAssessments: number;
  blockedAssessments: number;
  reviewAssessments: number;
  approvedAssessments: number;
  openAlerts: number;
  acknowledgedAlerts: number;
  totalDevices: number;
  blockedDevices: number;
  averageScore: number;
  protectedRevenueCents: number;
  preventedChargebacks: number;
};

export type AssessmentItem = {
  assessmentId: string;
  entityType: string;
  entityId: string;
  assessmentType: string;
  score: number;
  decision: "APPROVE" | "REVIEW" | "BLOCK";
  riskLevel: "LOW" | "MEDIUM" | "HIGH";
  reasonCodes: string[];
  createdAt: string;
};

export type DeviceItem = {
  deviceId: string;
  trustLevel: string;
  lastRiskLevel: string;
  seenCount: number;
  blocked: boolean;
  lastSeenAt: string;
};

type ApiResponse<T> = {
  data: T;
};

const fallbackSummary: DashboardSummary = {
  totalAssessments: 12,
  blockedAssessments: 5,
  reviewAssessments: 4,
  approvedAssessments: 3,
  openAlerts: 4,
  acknowledgedAlerts: 2,
  totalDevices: 7,
  blockedDevices: 3,
  averageScore: 61,
  protectedRevenueCents: 425900,
  preventedChargebacks: 14,
};

const fallbackAssessments: AssessmentItem[] = [
  {
    assessmentId: "fallback-1",
    entityType: "ORDER",
    entityId: "ord-demo-1",
    assessmentType: "ORDER_ANALYSIS",
    score: 90,
    decision: "BLOCK",
    riskLevel: "HIGH",
    reasonCodes: ["Multiple Accounts", "New Device", "Vpn Detected"],
    createdAt: new Date().toISOString(),
  },
  {
    assessmentId: "fallback-2",
    entityType: "CUSTOMER",
    entityId: "cus-demo-2",
    assessmentType: "LOGIN_ANALYSIS",
    score: 54,
    decision: "REVIEW",
    riskLevel: "MEDIUM",
    reasonCodes: ["Unusual Country", "New Device"],
    createdAt: new Date(Date.now() - 1000 * 60 * 9).toISOString(),
  },
  {
    assessmentId: "fallback-3",
    entityType: "CUSTOMER",
    entityId: "lead-demo-3",
    assessmentType: "REGISTER_ANALYSIS",
    score: 76,
    decision: "BLOCK",
    riskLevel: "HIGH",
    reasonCodes: ["Disposable Email", "Mass Signup"],
    createdAt: new Date(Date.now() - 1000 * 60 * 21).toISOString(),
  },
];

const fallbackDevices: DeviceItem[] = [
  {
    deviceId: "dev-demo-001",
    trustLevel: "HIGH",
    lastRiskLevel: "HIGH",
    seenCount: 4,
    blocked: true,
    lastSeenAt: new Date().toISOString(),
  },
  {
    deviceId: "dev-demo-002",
    trustLevel: "MEDIUM",
    lastRiskLevel: "MEDIUM",
    seenCount: 2,
    blocked: false,
    lastSeenAt: new Date(Date.now() - 1000 * 60 * 14).toISOString(),
  },
];

async function getJson<T>(path: string): Promise<T> {
  const apiBaseUrl = process.env.NEXT_PUBLIC_API_BASE_URL ?? "http://localhost:8080/api/v1";
  const apiKey = process.env.FRAUDSHIELD_API_KEY ?? "change-me";
  const tenantId = process.env.FRAUDSHIELD_TENANT_ID ?? "demo-store";

  const response = await fetch(`${apiBaseUrl}${path}`, {
    headers: {
      "X-API-Key": apiKey,
      "X-Tenant-Id": tenantId,
    },
    cache: "no-store",
  });

  if (!response.ok) {
    throw new Error(`Request failed: ${response.status}`);
  }

  const payload = (await response.json()) as ApiResponse<T>;
  return payload.data;
}

export async function getDashboardData() {
  try {
    const [summary, assessments, devices] = await Promise.all([
      getJson<DashboardSummary>("/dashboard/summary"),
      getJson<AssessmentItem[]>("/assessments"),
      getJson<DeviceItem[]>("/devices"),
    ]);

    return {
      summary,
      assessments,
      devices,
      source: "live" as const,
    };
  } catch {
    return {
      summary: fallbackSummary,
      assessments: fallbackAssessments,
      devices: fallbackDevices,
      source: "fallback" as const,
    };
  }
}
