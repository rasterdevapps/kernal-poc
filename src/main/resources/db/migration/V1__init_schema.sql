-- V1__init_schema.sql
-- Initial schema setup for ERP Kernel
-- Creates the initial kernel_info table.
-- Schema version tracking is handled by Flyway automatically.

-- Placeholder: first migration to verify Flyway integration is working.
-- Actual domain tables will be added in subsequent milestones.

CREATE TABLE IF NOT EXISTS kernel_info (
    id            BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    info_key      VARCHAR(255) NOT NULL UNIQUE,
    info_value    VARCHAR(1024),
    created_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO kernel_info (info_key, info_value) VALUES ('version', '0.0.1-SNAPSHOT');
INSERT INTO kernel_info (info_key, info_value) VALUES ('phase', 'Phase 1 — Foundation & Infrastructure Setup');
