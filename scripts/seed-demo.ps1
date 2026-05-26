$ErrorActionPreference = "Stop"

$headers = @{
    "Content-Type" = "application/json"
    "X-API-Key"    = "change-me"
    "X-Tenant-Id"  = "demo-store"
}

$requests = @(
    '{
      "orderId":"ord-seed-001",
      "customerId":"cus-seed-001",
      "amountCents":38990,
      "currency":"BRL",
      "recentAccount":true,
      "multipleAccountsDetected":true,
      "couponAbuseDetected":false,
      "unusualLocation":true,
      "device":{
        "deviceId":"dev-seed-001",
        "userAgent":"Mozilla/5.0",
        "browser":"Chrome",
        "operatingSystem":"Windows",
        "language":"pt-BR",
        "timezone":"America/Sao_Paulo",
        "ip":"127.0.0.1",
        "screenResolution":"1920x1080",
        "canvasFingerprint":"canvas-1",
        "webglFingerprint":"webgl-1",
        "newDevice":true,
        "vpnDetected":true,
        "proxyDetected":false,
        "torDetected":false,
        "automationDetected":false
      }
    }',
    '{
      "customerId":"cus-seed-002",
      "unusualCountry":true,
      "simultaneousLogin":false,
      "excessiveAttempts":true,
      "device":{
        "deviceId":"dev-seed-002",
        "userAgent":"Mozilla/5.0",
        "browser":"Edge",
        "operatingSystem":"Windows",
        "language":"pt-BR",
        "timezone":"America/Sao_Paulo",
        "ip":"127.0.0.1",
        "screenResolution":"1440x900",
        "canvasFingerprint":"canvas-2",
        "webglFingerprint":"webgl-2",
        "newDevice":true,
        "vpnDetected":false,
        "proxyDetected":false,
        "torDetected":false,
        "automationDetected":false
      }
    }',
    '{
      "email":"lead@tempmail.example",
      "cpf":"12345678900",
      "phone":"11999999999",
      "disposableEmail":true,
      "repeatedCpf":true,
      "suspiciousAddress":false,
      "reusedPhone":true,
      "massSignupDetected":true,
      "device":{
        "deviceId":"dev-seed-003",
        "userAgent":"Mozilla/5.0",
        "browser":"Chrome",
        "operatingSystem":"Linux",
        "language":"pt-BR",
        "timezone":"America/Sao_Paulo",
        "ip":"127.0.0.1",
        "screenResolution":"1366x768",
        "canvasFingerprint":"canvas-3",
        "webglFingerprint":"webgl-3",
        "newDevice":true,
        "vpnDetected":false,
        "proxyDetected":false,
        "torDetected":false,
        "automationDetected":true
      }
    }'
)

Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/v1/orders/analyze" -Headers $headers -Body $requests[0] | Out-Null
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/v1/login/analyze" -Headers $headers -Body $requests[1] | Out-Null
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/v1/register/analyze" -Headers $headers -Body $requests[2] | Out-Null

Write-Host "Dados demo enviados para a API."
