import http from "k6/http";
import { check } from "k6";

export const options = {
  stages: [
    { duration: "30s", target: 100 },
    { duration: "60s", target: 500 },
    { duration: "30s", target: 0 },
  ],
};

export default function () {
  const payload = JSON.stringify({
    orderId: `ord-${__VU}-${__ITER}`,
    customerId: `cus-${__VU}`,
    amountCents: 15990,
    currency: "BRL",
    recentAccount: true,
    multipleAccountsDetected: false,
    couponAbuseDetected: false,
    unusualLocation: true,
    device: {
      deviceId: `dev-${__VU}`,
      userAgent: "Mozilla/5.0",
      browser: "Chrome",
      operatingSystem: "Windows",
      language: "pt-BR",
      timezone: "America/Sao_Paulo",
      ip: "127.0.0.1",
      screenResolution: "1920x1080",
      canvasFingerprint: "canvas",
      webglFingerprint: "webgl",
      newDevice: true,
      vpnDetected: false,
      proxyDetected: false,
      torDetected: false,
      automationDetected: false
    }
  });

  const response = http.post("http://localhost:8080/api/v1/orders/analyze", payload, {
    headers: {
      "Content-Type": "application/json",
      "X-API-Key": "change-me",
      "X-Tenant-Id": "demo-store",
    },
  });

  check(response, {
    "status is 200": (r) => r.status === 200,
  });
}
