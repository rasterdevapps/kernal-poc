-- Phase 5: API & Communication Layer schema
-- Stores registered API clients for rate limiting and access tracking

CREATE TABLE api_clients (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id       VARCHAR(255) NOT NULL UNIQUE,
    client_name     VARCHAR(255) NOT NULL,
    enabled         BOOLEAN      NOT NULL DEFAULT TRUE,
    rate_limit      INT          NOT NULL DEFAULT 100,
    burst_capacity  INT          NOT NULL DEFAULT 200,
    created_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE api_access_log (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    client_id       VARCHAR(255),
    endpoint        VARCHAR(512) NOT NULL,
    method          VARCHAR(10)  NOT NULL,
    status_code     INT          NOT NULL,
    response_time   BIGINT,
    requested_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_api_access_log_client ON api_access_log (client_id);
CREATE INDEX idx_api_access_log_requested ON api_access_log (requested_at);
