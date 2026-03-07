-- V2__ddic_schema.sql
-- Phase 2: Database Architecture & Abstraction Layer
-- Creates DDIC (Data Dictionary) tables for the ANSI/SPARC three-schema architecture.

-- Domains define technical attributes (data type, length, value range)
CREATE TABLE ddic_domain (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    domain_name     VARCHAR(30) NOT NULL UNIQUE,
    data_type       VARCHAR(20) NOT NULL,
    max_length      INTEGER,
    decimal_places  INTEGER DEFAULT 0,
    description     VARCHAR(255),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Data Elements define semantic attributes and reference a domain
CREATE TABLE ddic_data_element (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    element_name    VARCHAR(30) NOT NULL UNIQUE,
    domain_id       BIGINT NOT NULL REFERENCES ddic_domain(id),
    short_label     VARCHAR(10),
    medium_label    VARCHAR(20),
    long_label      VARCHAR(40),
    description     VARCHAR(255),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table Definitions with schema level (ANSI/SPARC three-schema model)
CREATE TABLE ddic_table_definition (
    id                  BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    table_name          VARCHAR(30) NOT NULL UNIQUE,
    schema_level        VARCHAR(20) NOT NULL,
    description         VARCHAR(255),
    is_client_specific  BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_schema_level CHECK (schema_level IN ('EXTERNAL', 'CONCEPTUAL', 'INTERNAL'))
);

-- Table Fields (columns) referencing data elements
CREATE TABLE ddic_table_field (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    table_definition_id   BIGINT NOT NULL REFERENCES ddic_table_definition(id),
    field_name            VARCHAR(30) NOT NULL,
    data_element_id       BIGINT NOT NULL REFERENCES ddic_data_element(id),
    position              INTEGER NOT NULL,
    is_key                BOOLEAN NOT NULL DEFAULT FALSE,
    is_nullable           BOOLEAN NOT NULL DEFAULT TRUE,
    is_extension          BOOLEAN NOT NULL DEFAULT FALSE,
    created_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_table_field UNIQUE (table_definition_id, field_name)
);

-- Search Helps for field value lookups
CREATE TABLE ddic_search_help (
    id                    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    search_help_name      VARCHAR(30) NOT NULL UNIQUE,
    table_definition_id   BIGINT NOT NULL REFERENCES ddic_table_definition(id),
    description           VARCHAR(255),
    created_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Extension field values for client-specific "Z" fields (EAV pattern)
CREATE TABLE ddic_extension_field_value (
    id              BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    table_name      VARCHAR(30) NOT NULL,
    record_id       BIGINT NOT NULL,
    field_name      VARCHAR(30) NOT NULL,
    field_value     VARCHAR(1024),
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_extension_value UNIQUE (table_name, record_id, field_name)
);
