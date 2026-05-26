# FraudShield Commerce

Plataforma SaaS B2B multi-tenant de anti-fraude para e-commerce.

## Estrutura

- [docs/01-architecture.md](/c:/Users/mathe/Desktop/New/docs/01-architecture.md)
- [docs/02-business-model.md](/c:/Users/mathe/Desktop/New/docs/02-business-model.md)
- [docs/03-data-model.md](/c:/Users/mathe/Desktop/New/docs/03-data-model.md)
- [docs/04-backlog.md](/c:/Users/mathe/Desktop/New/docs/04-backlog.md)
- [docs/05-api-contracts.md](/c:/Users/mathe/Desktop/New/docs/05-api-contracts.md)
- [docs/06-roadmap-product.md](/c:/Users/mathe/Desktop/New/docs/06-roadmap-product.md)
- `services/risk-api`: backend Spring Boot
- `apps/web`: dashboard Next.js
- `infra`: Docker, Kubernetes, Helm e Terraform
- `scripts`: operacao local de desenvolvimento

## Como iniciar

### Backend

```bash
cd services/risk-api
mvn spring-boot:run
```

### Frontend

```bash
cd apps/web
npm install
npm run dev
```

### Scripts locais

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\start-local.ps1
powershell -ExecutionPolicy Bypass -File .\scripts\seed-demo.ps1
powershell -ExecutionPolicy Bypass -File .\scripts\stop-local.ps1
```

## Status

Este repositorio ja inclui:

- arquitetura, modelo SaaS, banco e backlog
- scaffold inicial production-grade
- risk engine com regras deterministicas iniciais
- endpoints principais do MVP
- dashboard executivo conectado a API local com fallback
- fluxo de alerts, assessments e dispositivos
- infraestrutura e pipeline inicial
