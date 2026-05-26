create extension if not exists "pgcrypto";

create table if not exists risk_assessments (
    id uuid primary key,
    tenant_id varchar(64) not null,
    entity_type varchar(32) not null,
    entity_id varchar(128) not null,
    assessment_type varchar(32) not null,
    score integer not null,
    decision varchar(16) not null,
    risk_level varchar(16) not null,
    reason_codes jsonb not null,
    created_at timestamp with time zone not null
);

create index if not exists idx_risk_assessments_tenant_entity
    on risk_assessments (tenant_id, entity_type, entity_id, created_at desc);

create table if not exists alerts (
    id uuid primary key,
    tenant_id varchar(64) not null,
    assessment_id uuid not null,
    severity varchar(16) not null,
    title varchar(255) not null,
    status varchar(16) not null,
    created_at timestamp with time zone not null
);

create index if not exists idx_alerts_tenant_created_at
    on alerts (tenant_id, created_at desc);

create table if not exists devices (
    id uuid primary key,
    tenant_id varchar(64) not null,
    device_id varchar(128) not null,
    fingerprint_hash varchar(128) not null,
    trust_level varchar(16) not null,
    last_seen_at timestamp with time zone not null,
    seen_count integer not null default 0,
    last_risk_level varchar(16) not null default 'LOW',
    blocked boolean not null default false,
    unique (tenant_id, device_id)
);
