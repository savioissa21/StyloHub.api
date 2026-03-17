package com.example.stylohub.domain.model.config;

import com.example.stylohub.domain.exception.DomainValidationException;
import com.example.stylohub.domain.model.WidgetType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LeadFormConfig implements WidgetConfig {

    private static final int MAX_FIELDS = 5;

    private final String title;
    private final String buttonLabel;
    private final String successMessage;
    private final List<String> fields; // ex: ["Nome", "Email", "Telefone"]

    @JsonCreator
    public LeadFormConfig(
            @JsonProperty("title") String title,
            @JsonProperty("buttonLabel") String buttonLabel,
            @JsonProperty("successMessage") String successMessage,
            @JsonProperty("fields") List<String> fields) {
        this.title = title;
        this.buttonLabel = buttonLabel;
        this.successMessage = successMessage;
        this.fields = fields != null ? List.copyOf(fields) : List.of();
        this.validate();
    }

    public String getTitle() { return title; }
    public String getButtonLabel() { return buttonLabel; }
    public String getSuccessMessage() { return successMessage; }
    public List<String> getFields() { return fields; }

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
}
