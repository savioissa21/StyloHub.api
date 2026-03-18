-- PRO theme customization fields
ALTER TABLE profiles
    ADD COLUMN IF NOT EXISTS border_color  VARCHAR(7)  NOT NULL DEFAULT '#D4AF37',
    ADD COLUMN IF NOT EXISTS shadow_style  VARCHAR(20) NOT NULL DEFAULT 'NONE';
