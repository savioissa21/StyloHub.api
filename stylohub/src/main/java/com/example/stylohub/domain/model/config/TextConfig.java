package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TextConfig implements WidgetConfig {

    private static final int MAX_CONTENT_LENGTH = 500;

    private final String title;
    private final String content;

    @JsonCreator
    public TextConfig(
            @JsonProperty("title") String title,
            @JsonProperty("content") String content) {
        this.title = title;
        this.content = content;
        this.validate();
    }

    public String getTitle() { return title; }
    public String getContent() { return content; }

    @Override
    public WidgetType getType() {
        return WidgetType.TEXT;
    }

    @Override
    public void validate() {
        if (content == null || content.isBlank()) {
            throw new DomainValidationException("O conteúdo do widget de texto não pode estar vazio.");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new DomainValidationException(
                "O conteúdo do widget de texto não pode ultrapassar " + MAX_CONTENT_LENGTH + " caracteres."
            );
        }
    }
}
