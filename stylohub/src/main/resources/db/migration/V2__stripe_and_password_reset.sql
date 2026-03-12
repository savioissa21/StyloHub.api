-- ============================================================
-- V2: Integração Stripe + Password Reset
-- ============================================================

-- Colunas Stripe na tabela users
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS stripe_customer_id     VARCHAR(255),
    ADD COLUMN IF NOT EXISTS stripe_subscription_id VARCHAR(255);

CREATE UNIQUE INDEX IF NOT EXISTS uq_users_stripe_customer
    ON users (stripe_customer_id) WHERE stripe_customer_id IS NOT NULL;

-- ============================================================
-- Tabela: password_reset_tokens
-- ============================================================
CREATE TABLE password_reset_tokens (
    id         UUID        NOT NULL DEFAULT gen_random_uuid(),
    user_id    UUID        NOT NULL,
    token      VARCHAR(64) NOT NULL,
    expires_at TIMESTAMPTZ NOT NULL,
    used       BOOLEAN     NOT NULL DEFAULT FALSE,

    CONSTRAINT pk_password_reset_tokens  PRIMARY KEY (id),
    CONSTRAINT uq_password_reset_token   UNIQUE (token),
    CONSTRAINT fk_password_reset_user    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE INDEX idx_password_reset_token   ON password_reset_tokens (token);
CREATE INDEX idx_password_reset_user_id ON password_reset_tokens (user_id);
