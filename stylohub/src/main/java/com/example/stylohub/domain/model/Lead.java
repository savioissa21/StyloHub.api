package com.example.stylohub.domain.model;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

/**
 * Aggregate: um lead capturado através de um LEAD_FORM widget na página pública.
 */
public class Lead {

    private final UUID id;
    private final UUID profileId;
    private final UUID widgetId;
    private final String widgetTitle;
    private final Map<String, String> fields; // ex: {"Email": "user@x.com", "Nome": "João"}
    private final LocalDateTime capturedAt;

    public Lead(UUID id, UUID profileId, UUID widgetId, String widgetTitle,
                Map<String, String> fields, LocalDateTime capturedAt) {
        this.id = id;
        this.profileId = profileId;
        this.widgetId = widgetId;
        this.widgetTitle = widgetTitle;
        this.fields = Map.copyOf(fields);
        this.capturedAt = capturedAt;
    }

    public UUID getId()           { return id; }
    public UUID getProfileId()    { return profileId; }
    public UUID getWidgetId()     { return widgetId; }
    public String getWidgetTitle(){ return widgetTitle; }
    public Map<String, String> getFields() { return fields; }
    public LocalDateTime getCapturedAt()   { return capturedAt; }

    /** Extrai o e-mail do mapa de campos (case-insensitive na chave). */
    public String getEmail() {
        return fields.entrySet().stream()
                .filter(e -> e.getKey().toLowerCase().contains("email")
                          || e.getKey().toLowerCase().contains("e-mail"))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse("");
    }
}
