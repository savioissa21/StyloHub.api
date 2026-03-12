package com.example.stylohub.adapter.out.email;

import com.example.stylohub.application.port.out.EmailPort;
import com.example.stylohub.infrastructure.config.properties.ResendProperties;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ResendEmailAdapter implements EmailPort {

    private static final Logger log = LoggerFactory.getLogger(ResendEmailAdapter.class);

    private final Resend resend;
    private final ResendProperties props;

    public ResendEmailAdapter(ResendProperties props) {
        this.props = props;
        this.resend = new Resend(props.apiKey());
    }

    @Override
    public void sendPasswordReset(String toEmail, String resetLink) {
        send(toEmail,
             "Redefinição de senha - StyloHub",
             passwordResetHtml(resetLink));
    }

    @Override
    public void sendWelcome(String toEmail, String username) {
        send(toEmail,
             "Bem-vindo ao StyloHub, " + username + "! 🎉",
             welcomeHtml(username));
    }

    @Override
    public void sendPaymentFailed(String toEmail) {
        send(toEmail,
             "Problema com o teu pagamento - StyloHub",
             paymentFailedHtml());
    }

    private void send(String to, String subject, String html) {
        try {
            CreateEmailOptions options = CreateEmailOptions.builder()
                    .from(props.fromName() + " <" + props.fromEmail() + ">")
                    .to(to)
                    .subject(subject)
                    .html(html)
                    .build();
            resend.emails().send(options);
            log.info("[EMAIL] Enviado para={} subject={}", to, subject);
        } catch (ResendException e) {
            log.error("[EMAIL] Falha ao enviar email para={}: {}", to, e.getMessage());
            // Não propagamos para não bloquear o fluxo principal
        }
    }

    // --- Templates HTML ---

    private String passwordResetHtml(String resetLink) {
        return """
            <div style="font-family:sans-serif;max-width:520px;margin:0 auto">
              <h2>Redefinição de senha</h2>
              <p>Recebemos um pedido para redefinir a senha da tua conta StyloHub.</p>
              <p>
                <a href="%s"
                   style="background:#000;color:#fff;padding:12px 24px;border-radius:6px;text-decoration:none;display:inline-block">
                  Redefinir senha
                </a>
              </p>
              <p style="color:#666;font-size:13px">
                Este link expira em 30 minutos.<br>
                Se não pediste a redefinição, ignora este email.
              </p>
            </div>
            """.formatted(resetLink);
    }

    private String welcomeHtml(String username) {
        return """
            <div style="font-family:sans-serif;max-width:520px;margin:0 auto">
              <h2>Bem-vindo ao StyloHub! 🎉</h2>
              <p>Olá <strong>%s</strong>, a tua conta foi criada com sucesso.</p>
              <p>O teu link já está ativo em <a href="https://stylohub.io/%s">stylohub.io/%s</a></p>
              <p>Começa já a personalizar o teu perfil!</p>
            </div>
            """.formatted(username, username, username);
    }

    private String paymentFailedHtml() {
        return """
            <div style="font-family:sans-serif;max-width:520px;margin:0 auto">
              <h2>Problema com o teu pagamento</h2>
              <p>Não conseguimos processar o pagamento da tua subscrição PRO.</p>
              <p>Verifica os dados do teu cartão e tenta novamente.</p>
              <p>
                <a href="https://stylohub.io/dashboard/billing"
                   style="background:#000;color:#fff;padding:12px 24px;border-radius:6px;text-decoration:none;display:inline-block">
                  Atualizar dados de pagamento
                </a>
              </p>
            </div>
            """;
    }
}
