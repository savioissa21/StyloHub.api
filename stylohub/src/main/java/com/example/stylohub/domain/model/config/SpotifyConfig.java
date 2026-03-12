package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;

public class SpotifyConfig implements WidgetConfig {

    // Formato: spotify:track:4iV5W9uYEdYUVa79Axb7Rh ou spotify:album:... ou spotify:playlist:...
    private final String embedUri;
    private final boolean compact;

    public SpotifyConfig(String embedUri, boolean compact) {
        this.embedUri = embedUri;
        this.compact = compact;
        this.validate();
    }

    @Override
    public WidgetType getType() {
        return WidgetType.SPOTIFY;
    }

    @Override
    public void validate() {
        if (embedUri == null || embedUri.isBlank()) {
            throw new DomainValidationException("O URI do Spotify não pode estar vazio.");
        }
        if (!embedUri.startsWith("spotify:track:") &&
            !embedUri.startsWith("spotify:album:") &&
            !embedUri.startsWith("spotify:playlist:")) {
            throw new DomainValidationException(
                "URI do Spotify inválido. Use o formato: spotify:track:ID, spotify:album:ID ou spotify:playlist:ID"
            );
        }
    }

    public String getEmbedUri() { return embedUri; }
    public boolean isCompact() { return compact; }
}
