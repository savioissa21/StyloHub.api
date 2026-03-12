package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;

public class TextConfig implements WidgetConfig {

    private static final int MAX_CONTENT_LENGTH = 500;

    private final String title;
    private final String content;

    public TextConfig(String title, String content) {
        this.title = title;
        this.content = content;
        this.validate();
    }

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

    public String getTitle() { return title; }
    public String getContent() { return content; }
}
