-- Leads capturados via widgets LEAD_FORM
CREATE TABLE IF NOT EXISTS leads (
    id            UUID        NOT NULL DEFAULT gen_random_uuid(),
    profile_id    UUID        NOT NULL,
    widget_id     UUID        NOT NULL,
    widget_title  VARCHAR(255),
    fields_json   JSONB       NOT NULL DEFAULT '{}',
    captured_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),

    CONSTRAINT pk_leads            PRIMARY KEY (id),
    CONSTRAINT fk_leads_profile    FOREIGN KEY (profile_id) REFERENCES profiles (id) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_leads_profile_id   ON leads (profile_id);
CREATE INDEX IF NOT EXISTS idx_leads_captured_at  ON leads (profile_id, captured_at DESC);
