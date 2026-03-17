package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageConfig implements WidgetConfig {

    private final String imageUrl;
    private final String altText;
    private final String linkUrl; // Opcional: imagem clicável

    @JsonCreator
    public ImageConfig(
            @JsonProperty("imageUrl") String imageUrl,
            @JsonProperty("altText") String altText,
            @JsonProperty("linkUrl") String linkUrl) {
        this.imageUrl = imageUrl;
        this.altText = altText;
        this.linkUrl = linkUrl;
        this.validate();
    }

    public String getImageUrl() { return imageUrl; }
    public String getAltText() { return altText; }
    public String getLinkUrl() { return linkUrl; }

    @Override
    public WidgetType getType() {
        return WidgetType.IMAGE;
    }

    @Override
    public void validate() {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new DomainValidationException("A URL da imagem não pode estar vazia.");
        }
        if (!imageUrl.startsWith("http://") && !imageUrl.startsWith("https://")) {
            throw new DomainValidationException("A URL da imagem deve começar com http:// ou https://");
        }
        if (altText == null || altText.isBlank()) {
            throw new DomainValidationException("O texto alternativo da imagem é obrigatório para acessibilidade.");
        }
        if (linkUrl != null && !linkUrl.isBlank()) {
            if (!linkUrl.startsWith("http://") && !linkUrl.startsWith("https://")) {
                throw new DomainValidationException("A URL de destino da imagem deve começar com http:// ou https://");
            }
        }
    }
}
