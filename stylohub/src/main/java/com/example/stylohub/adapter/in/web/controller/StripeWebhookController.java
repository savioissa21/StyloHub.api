package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.application.port.in.ManageProfileUseCase;
import com.example.stylohub.application.port.out.ProfileRepositoryPort;
import com.example.stylohub.application.port.out.UserRepositoryPort;
import com.example.stylohub.domain.model.PlanType;
import com.example.stylohub.domain.model.Profile;
import com.example.stylohub.domain.model.Subscription;
import com.example.stylohub.infrastructure.config.properties.StripeProperties;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/webhooks/stripe")
@Tag(name = "Webhooks", description = "Endpoints internos de webhooks")
public class StripeWebhookController {

    private static final Logger log = LoggerFactory.getLogger(StripeWebhookController.class);

    private final StripeProperties stripeProps;
    private final ManageProfileUseCase profileUseCase;
    private final UserRepositoryPort userRepo;

    public StripeWebhookController(StripeProperties stripeProps,
                                   ManageProfileUseCase profileUseCase,
                                   UserRepositoryPort userRepo) {
        this.stripeProps = stripeProps;
        this.profileUseCase = profileUseCase;
        this.userRepo = userRepo;
    }

    @PostMapping
    @Operation(summary = "Recebe eventos do Stripe (checkout, cancelamento)")
    ResponseEntity<Void> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, stripeProps.webhookSecret());
        } catch (SignatureVerificationException e) {
            log.warn("[STRIPE] Assinatura inválida recebida: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        log.info("[STRIPE] Evento recebido: {}", event.getType());

        switch (event.getType()) {
            case "checkout.session.completed" -> handleCheckoutCompleted(event);
            case "customer.subscription.deleted" -> handleSubscriptionCanceled(event);
            case "invoice.payment_failed" -> handlePaymentFailed(event);
            default -> log.debug("[STRIPE] Evento ignorado: {}", event.getType());
        }

        return ResponseEntity.ok().build();
    }

    private void handleCheckoutCompleted(Event event) {
        Session session = (Session) event.getDataObjectDeserializer()
                .getObject().orElseThrow();

        String userId = session.getMetadata().get("userId");
        if (userId == null) {
            log.error("[STRIPE] checkout.session.completed sem userId nos metadados");
            return;
        }

        try {
            Profile profile = profileUseCase.getProfileByUserId(UUID.fromString(userId));
            profile.upgradeSubscription(new Subscription(PlanType.PRO));
            profileUseCase.upgradeSubscription(profile.getId(), PlanType.PRO);
            log.info("[STRIPE] Upgrade PRO aplicado para userId={}", userId);
        } catch (Exception e) {
            log.error("[STRIPE] Falha ao ativar PRO para userId={}: {}", userId, e.getMessage());
        }
    }

    private void handleSubscriptionCanceled(Event event) {
        com.stripe.model.Subscription subscription = (com.stripe.model.Subscription)
                event.getDataObjectDeserializer().getObject().orElseThrow();

        String userId = subscription.getMetadata().get("userId");
        if (userId == null) return;

        try {
            profileUseCase.upgradeSubscription(
                    profileUseCase.getProfileByUserId(UUID.fromString(userId)).getId(),
                    PlanType.FREE
            );
            log.info("[STRIPE] Downgrade para FREE aplicado para userId={}", userId);
        } catch (Exception e) {
            log.error("[STRIPE] Falha ao rebaixar plano para userId={}: {}", userId, e.getMessage());
        }
    }

    private void handlePaymentFailed(Event event) {
        Invoice invoice = (Invoice) event.getDataObjectDeserializer().getObject().orElseThrow();
        log.warn("[STRIPE] Pagamento falhou para customer={}", invoice.getCustomer());
        // TODO: Notificar utilizador por email via ResendEmailAdapter
    }
}
