-- ============================================================
-- StyloHub - Schema inicial
-- ============================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- ============================================================
-- Tabela: users
-- ============================================================
CREATE TABLE users (
    id           UUID         NOT NULL DEFAULT gen_random_uuid(),
    email        VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255),
    provider     VARCHAR(20)  NOT NULL DEFAULT 'LOCAL',
    is_active    BOOLEAN      NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT chk_users_provider CHECK (provider IN ('LOCAL', 'GOOGLE', 'APPLE'))
);

-- ============================================================
-- Tabela: profiles
-- ============================================================
CREATE TABLE profiles (
    id              UUID        NOT NULL DEFAULT gen_random_uuid(),
    user_id         UUID        NOT NULL,
    username        VARCHAR(50) NOT NULL,
    bg_type         VARCHAR(20) NOT NULL DEFAULT 'SOLID_COLOR',
    bg_value        VARCHAR(255) NOT NULL DEFAULT '#FFFFFF',
    primary_color   VARCHAR(7)  NOT NULL DEFAULT '#000000',
    text_color      VARCHAR(7)  NOT NULL DEFAULT '#FFFFFF',
    button_style    VARCHAR(20) NOT NULL DEFAULT 'ROUNDED',
    is_custom_theme BOOLEAN     NOT NULL DEFAULT FALSE,
    plan_type       VARCHAR(20) NOT NULL DEFAULT 'FREE',

    CONSTRAINT pk_profiles        PRIMARY KEY (id),
    CONSTRAINT uq_profiles_username UNIQUE (username),
    CONSTRAINT fk_profiles_user   FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_profiles_plan  CHECK (plan_type IN ('FREE', 'PRO')),
    CONSTRAINT chk_profiles_bg    CHECK (bg_type IN ('SOLID_COLOR', 'GRADIENT', 'IMAGE')),
    CONSTRAINT chk_profiles_btn   CHECK (button_style IN ('ROUNDED', 'PILL', 'SQUARED', 'OUTLINE', 'HARD_SHADOW'))
);

CREATE INDEX idx_profiles_user_id  ON profiles (user_id);
CREATE INDEX idx_profiles_username ON profiles (username);

-- ============================================================
-- Tabela: widgets
-- ============================================================
CREATE TABLE widgets (
    id          UUID        NOT NULL DEFAULT gen_random_uuid(),
    profile_id  UUID        NOT NULL,
    widget_type VARCHAR(20) NOT NULL,
    config      JSONB       NOT NULL DEFAULT '{}',
    order_index INT         NOT NULL DEFAULT 0,
    is_active   BOOLEAN     NOT NULL DEFAULT TRUE,

    CONSTRAINT pk_widgets         PRIMARY KEY (id),
    CONSTRAINT fk_widgets_profile FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE,
    CONSTRAINT chk_widgets_type   CHECK (widget_type IN ('LINK', 'YOUTUBE', 'SPOTIFY', 'IMAGE', 'TEXT', 'LEAD_FORM'))
);

CREATE INDEX idx_widgets_profile_id   ON widgets (profile_id);
CREATE INDEX idx_widgets_order_index  ON widgets (profile_id, order_index);
CREATE INDEX idx_widgets_config_gin   ON widgets USING GIN (config);

-- ============================================================
-- Tabela: analytics_events
-- ============================================================
CREATE TABLE analytics_events (
    id          BIGSERIAL    NOT NULL,
    profile_id  UUID         NOT NULL,
    widget_id   UUID,
    event_type  VARCHAR(20)  NOT NULL DEFAULT 'PROFILE_VIEW',
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_analytics    PRIMARY KEY (id),
    CONSTRAINT chk_event_type  CHECK (event_type IN ('PROFILE_VIEW', 'WIDGET_CLICK'))
);

CREATE INDEX idx_analytics_profile_id ON analytics_events (profile_id);
CREATE INDEX idx_analytics_created_at ON analytics_events (created_at DESC);
