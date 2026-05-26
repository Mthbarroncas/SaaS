# FraudShield Commerce

FraudShield Commerce e uma plataforma SaaS B2B multi-tenant de anti-fraude para e-commerce, desenhada para reduzir chargebacks, fraude em pagamentos, account takeover, abuso promocional e automacao maliciosa com decisoes em tempo real.

## O que o produto entrega

- risk score em tempo real para pedido, login, cadastro e cliente
- decisao automatica `APPROVE`, `REVIEW` e `BLOCK`
- device intelligence com historico e confiabilidade
- dashboard executivo com KPIs operacionais
- regras deterministicas iniciais com explainability
- base de infraestrutura para Docker, Kubernetes, Helm e Terraform

## Stack

- frontend: Next.js, React, TypeScript, Tailwind CSS
- backend: Java 21, Spring Boot 3, Spring Security, OpenAPI
- dados: PostgreSQL, Redis, Kafka
- observabilidade: Prometheus, Grafana, ELK
- infra: Docker, Kubernetes, Helm, Terraform

## Estrutura

- [docs/01-architecture.md](docs/01-architecture.md)
- [docs/02-business-model.md](docs/02-business-model.md)
- [docs/03-data-model.md](docs/03-data-model.md)
- [docs/04-backlog.md](docs/04-backlog.md)
- [docs/05-api-contracts.md](docs/05-api-contracts.md)
- [docs/06-roadmap-product.md](docs/06-roadmap-product.md)
- [docs/deployment/README.md](docs/deployment/README.md)
- `apps/web`: dashboard Next.js
- `services/risk-api`: API Spring Boot
- `infra`: Docker, K8s, Helm, Terraform e base AWS
- `scripts`: start, seed e stop do ambiente local

## Como rodar localmente

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\start-local.ps1
powershell -ExecutionPolicy Bypass -File .\scripts\seed-demo.ps1
```

Ambiente local:

- frontend: `http://localhost:3000`
- backend: `http://localhost:8080`
- health: `http://localhost:8080/actuator/health`
- swagger: `http://localhost:8080/swagger-ui/index.html`

Para encerrar:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\stop-local.ps1
```

## Deploy

Base preparada para:

- Vercel: [apps/web/vercel.json](apps/web/vercel.json)
- Render: [render.yaml](render.yaml)
- Railway: [railway.toml](railway.toml)
- AWS ECS Fargate: [infra/aws/ecs/task-definition-risk-api.json](infra/aws/ecs/task-definition-risk-api.json)

Guia:

- [docs/deployment/README.md](docs/deployment/README.md)

## Estado atual

Este repositorio ja inclui:

- arquitetura, modelo SaaS, banco e roadmap
- backend MVP com endpoints principais de analise
- dashboard integrado a API local com fallback
- fluxo de alerts, assessments e devices
- scripts de operacao local
- pipeline CI inicial

## Proximo passo recomendado

- conectar o dashboard a acoes reais de acknowledge e filtros
- trocar regras hardcoded por regras persistidas por tenant
- ativar Redis, Kafka e PostgreSQL reais em ambiente cloud
- publicar a primeira release produtizada com observabilidade e secrets gerenciados
