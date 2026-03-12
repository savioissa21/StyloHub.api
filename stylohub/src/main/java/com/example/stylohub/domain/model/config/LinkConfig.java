package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.model.WidgetType;

public class LinkConfig implements WidgetConfig {

    private final String title;
    private final String url;

    public LinkConfig(String title, String url) {
        this.title = title;
        this.url = url;
        this.validate(); // Valida no momento da criação!
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public WidgetType getType() {
        return WidgetType.LINK;
    }

    @Override
    public void validate() {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("O título do link não pode estar vazio.");
        }
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("A URL não pode estar vazia.");
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new IllegalArgumentException("A URL deve começar com http:// ou https://");
        }
    }
}