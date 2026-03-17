package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LinkConfig implements WidgetConfig {

    private final String title;
    private final String url;

    @JsonCreator
    public LinkConfig(
            @JsonProperty("title") String title,
            @JsonProperty("url") String url) {
        this.title = title;
        this.url = url;
        this.validate();
    }

    public String getTitle() { return title; }
    public String getUrl() { return url; }

    @Override
    public WidgetType getType() {
        return WidgetType.LINK;
    }

    @Override
    public void validate() {
        if (title == null || title.isBlank()) {
            throw new DomainValidationException("O título do link não pode estar vazio.");
        }
        if (url == null || url.isBlank()) {
            throw new DomainValidationException("A URL não pode estar vazia.");
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new DomainValidationException("A URL deve começar com http:// ou https://");
        }
    }
}
