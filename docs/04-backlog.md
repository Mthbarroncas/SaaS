# FraudShield Commerce - Backlog

## MVP

### Foundation

1. Criar monorepo com apps frontend, backend e infraestrutura
2. Implementar autenticacao JWT para dashboard e API keys para merchants
3. Implementar multi-tenancy por `tenant_id`
4. Configurar observabilidade base, logs estruturados e tracing IDs
5. Configurar CI/CD, quality gates e scans de seguranca

### Core Risk

6. Implementar `POST /events`
7. Implementar `POST /orders/analyze`
8. Implementar `POST /login/analyze`
9. Implementar `POST /register/analyze`
10. Implementar `POST /customers/analyze`
11. Implementar `GET /risk-score`, `GET /alerts`, `GET /devices`
12. Implementar risk engine com pesos, thresholds e reason codes
13. Implementar regras customizadas em JSON/simple DSL

### Intelligence

14. Implementar device fingerprint collector web
15. Implementar trust score do device
16. Implementar detector basico de VPN/Proxy/TOR por provider externo configuravel
17. Implementar detector de email descartavel
18. Implementar velocity checks para cadastro, login e pedido
19. Implementar score de bot basico

### Dashboard

20. Dashboard executivo com KPIs
21. Timeline de eventos com filtros
22. Lista de pedidos com status approve/review/block
23. Lista de dispositivos suspeitos
24. Tela de regras com CRUD

### Security & Compliance

25. MFA para painel
26. trilha de auditoria
27. CSP, headers, CSRF e rate limiting
28. mascaramento de PII e hashing de atributos sensiveis

### QA

29. testes unitarios do motor de risco
30. testes de integracao da API
31. smoke E2E do dashboard
32. teste de carga para 100k eventos/min

## Enterprise

1. SSO/SAML
2. tenancy dedicada por namespace
3. case management completo com SLA
4. simulador de regras visual
5. explainability avancada do score
6. machine learning supervisionado
7. feedback loop de chargeback
8. integracao com gateways/PSPs
9. webhooks assinados
10. data residency e BYOK
11. retention policies customizadas
12. alertas para Slack/Teams/Email/SIEM
13. graph de entidades e link analysis
14. deteccao avancada de bot e emulacao
15. score de abuso de cupom e loyalty fraud
