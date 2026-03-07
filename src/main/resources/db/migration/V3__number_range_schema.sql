-- V3__number_range_schema.sql
-- Phase 3: Core Framework, Data Types & System Variables
-- Creates Number Range tables for SAP-style sequential number assignment.

-- Number Range objects
CREATE TABLE number_range (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    range_object    VARCHAR(30) NOT NULL UNIQUE,
    description     VARCHAR(255),
    is_buffered     BOOLEAN NOT NULL DEFAULT FALSE,
    buffer_size     INTEGER,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Number Range intervals
CREATE TABLE number_range_interval (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    number_range_id     BIGINT NOT NULL REFERENCES number_range(id) ON DELETE CASCADE,
    sub_object          VARCHAR(2) NOT NULL,
    from_number         VARCHAR(20) NOT NULL,
    to_number           VARCHAR(20) NOT NULL,
    current_number      VARCHAR(20) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_number_range_interval UNIQUE (number_range_id, sub_object)
);
