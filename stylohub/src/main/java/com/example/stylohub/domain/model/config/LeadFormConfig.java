package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;

import java.util.List;

public class LeadFormConfig implements WidgetConfig {

    private static final int MAX_FIELDS = 5;

    private final String title;
    private final String buttonLabel;
    private final String successMessage;
    private final List<String> fields; // ex: ["Nome", "Email", "Telefone"]

    public LeadFormConfig(String title, String buttonLabel, String successMessage, List<String> fields) {
        this.title = title;
        this.buttonLabel = buttonLabel;
        this.successMessage = successMessage;
        this.fields = fields != null ? List.copyOf(fields) : List.of();
        this.validate();
    }

    @Override
    public WidgetType getType() {
        return WidgetType.LEAD_FORM;
    }

    @Override
    public void validate() {
        if (title == null || title.isBlank()) {
            throw new DomainValidationException("O título do formulário de lead é obrigatório.");
        }
        if (buttonLabel == null || buttonLabel.isBlank()) {
            throw new DomainValidationException("O rótulo do botão do formulário é obrigatório.");
        }
        if (fields.isEmpty()) {
            throw new DomainValidationException("O formulário deve ter pelo menos um campo.");
        }
        if (fields.size() > MAX_FIELDS) {
            throw new DomainValidationException(
                "O formulário não pode ter mais de " + MAX_FIELDS + " campos."
            );
        }
        boolean hasEmailField = fields.stream()
                .anyMatch(f -> f.toLowerCase().contains("email") || f.toLowerCase().contains("e-mail"));
        if (!hasEmailField) {
            throw new DomainValidationException("O formulário de lead deve conter um campo de e-mail.");
        }
    }

    public String getTitle() { return title; }
    public String getButtonLabel() { return buttonLabel; }
    public String getSuccessMessage() { return successMessage; }
    public List<String> getFields() { return fields; }
}
