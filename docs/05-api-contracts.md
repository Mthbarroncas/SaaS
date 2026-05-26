# FraudShield Commerce - API Contracts

## Autenticacao

- merchants: `X-API-Key` + `X-Signature`
- dashboard: OAuth2 password / authorization code com JWT

## Endpoints Publicos

### POST /api/v1/events

Recebe eventos de comportamento, autenticacao, navegacao e device.

### POST /api/v1/orders/analyze

Recebe contexto do pedido e retorna score e decisao.

### POST /api/v1/customers/analyze

Retorna risco agregado do cliente.

### POST /api/v1/login/analyze

Analisa tentativa de login.

### POST /api/v1/register/analyze

Analisa tentativa de cadastro.

### GET /api/v1/risk-score/{assessmentId}

Retorna score detalhado.

### GET /api/v1/alerts

Lista alertas do tenant.

### GET /api/v1/devices

Lista dispositivos do tenant.

## Contrato de Resposta Padrao

```json
{
  "requestId": "b4ef8dcb-6ce8-46c4-92c5-c8cd8408f91a",
  "timestamp": "2026-05-26T13:00:00Z",
  "data": {},
  "errors": []
}
```

## Contrato de Decisao

```json
{
  "assessmentId": "f5a299d0-d630-4ff7-aed1-ec4dcb66b0d6",
  "score": 72,
  "decision": "BLOCK",
  "riskLevel": "HIGH",
  "reasonCodes": [
    "NEW_DEVICE",
    "VPN_DETECTED",
    "DISPOSABLE_EMAIL"
  ],
  "reviewRequired": false
}
```
