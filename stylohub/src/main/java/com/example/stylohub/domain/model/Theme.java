package com.example.stylohub.domain.model;

/**
 * Value Object que define o design da página do utilizador.
 */
public class Theme {

    private final BackgroundType bgType;
    private final String bgValue; // Pode ser um HEX (#FFF), um CSS Gradient ou uma URL de imagem
    private final String primaryColor;
    private final String textColor;
    private final ButtonStyle buttonStyle;
    private final boolean isCustom; // Define se é um tema predefinido do sistema ou customizado pelo utilizador

    // PRO fields
    private final String borderColor;   // Cor da borda dos botões (hex)
    private final ShadowStyle shadowStyle; // Efeito de sombra nos botões

    public Theme(BackgroundType bgType, String bgValue, String primaryColor, String textColor,
                 ButtonStyle buttonStyle, boolean isCustom,
                 String borderColor, ShadowStyle shadowStyle) {
        if (bgType == null || bgValue == null || primaryColor == null || textColor == null
                || buttonStyle == null || borderColor == null || shadowStyle == null) {
            throw new IllegalArgumentException("Nenhum atributo do tema pode ser nulo.");
        }

        this.bgType = bgType;
        this.bgValue = bgValue;
        this.primaryColor = primaryColor;
        this.textColor = textColor;
        this.buttonStyle = buttonStyle;
        this.isCustom = isCustom;
        this.borderColor = borderColor;
        this.shadowStyle = shadowStyle;

        this.validateContrast();
    }

    /**
     * Regra de negócio: Impede configurações de design que quebrem a usabilidade.
     */
    private void validateContrast() {
        // Validação simples: Se a cor de fundo primária for igual à cor do texto, é impossível de ler.
        if (this.primaryColor.equalsIgnoreCase(this.textColor)) {
            throw new IllegalArgumentException("A cor do texto não pode ser igual à cor primária do botão.");
        }
    }

    public BackgroundType getBgType() { return bgType; }
    public String getBgValue() { return bgValue; }
    public String getPrimaryColor() { return primaryColor; }
    public String getTextColor() { return textColor; }
    public ButtonStyle getButtonStyle() { return buttonStyle; }
    public boolean isCustom() { return isCustom; }
    public String getBorderColor() { return borderColor; }
    public ShadowStyle getShadowStyle() { return shadowStyle; }
}