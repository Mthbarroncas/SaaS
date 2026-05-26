# Deployment Guide

## Visao geral

O repositório foi preparado para um modelo simples de deploy separado:

- `apps/web` em Vercel
- `services/risk-api` em Render, Railway ou AWS ECS
- PostgreSQL, Redis e Kafka gerenciados fora da aplicacao

## Variaveis principais

### Frontend

- `NEXT_PUBLIC_API_BASE_URL`
- `FRAUDSHIELD_API_KEY`
- `FRAUDSHIELD_TENANT_ID`

### Backend

- `SPRING_PROFILES_ACTIVE`
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SPRING_KAFKA_BOOTSTRAP_SERVERS`
- `FRAUDSHIELD_SECURITY_MERCHANT_API_KEY`
- `FRAUDSHIELD_SECURITY_DASHBOARD_BEARER_TOKEN`
- `FRAUDSHIELD_MESSAGING_KAFKA_ENABLED`

## Vercel

Deploy recomendado para o dashboard.

Configuracao base:

- root directory: `apps/web`
- framework: Next.js
- install command: `npm install`
- build command: `npm run build`

Arquivo pronto:

- [apps/web/vercel.json](../../apps/web/vercel.json)

## Render

O blueprint [render.yaml](../../render.yaml) cria:

- `fraudshield-web`
- `fraudshield-risk-api`

Para producao, ajuste:

- secrets
- dominio customizado
- banco gerenciado
- redis gerenciado
- kafka externo

## Railway

Base inicial em [railway.toml](../../railway.toml).

Uso sugerido:

- um service para `services/risk-api`
- um service para `apps/web`
- plugins gerenciados para PostgreSQL e Redis

## AWS

Base inicial para ECS Fargate:

- [infra/aws/ecs/task-definition-risk-api.json](../../infra/aws/ecs/task-definition-risk-api.json)
- [infra/aws/ecs/task-definition-web.json](../../infra/aws/ecs/task-definition-web.json)

Arquitetura sugerida:

- ALB
- ECS Fargate
- RDS PostgreSQL
- ElastiCache Redis
- MSK ou Confluent Cloud para Kafka
- Secrets Manager / Parameter Store

## Observacoes

- o perfil `local` desabilita Kafka para facilitar desenvolvimento
- para producao, use PostgreSQL real e `SPRING_PROFILES_ACTIVE=default`
- o dashboard aceita fallback local, mas em producao deve apontar para a API publicada
