package com.example.stylohub.adapter.out.payment;

import com.example.stylohub.application.port.out.SubscriptionPaymentPort;
import com.example.stylohub.infrastructure.config.properties.StripeProperties;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StripeSubscriptionAdapter implements SubscriptionPaymentPort {

    private final StripeProperties stripeProps;

    public StripeSubscriptionAdapter(StripeProperties stripeProps) {
        this.stripeProps = stripeProps;
    }

    @PostConstruct
    void init() {
        Stripe.apiKey = stripeProps.secretKey();
    }

    @Override
    public String createCheckoutSession(UUID userId, String userEmail,
                                        String successUrl, String cancelUrl) {
        try {
            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setCustomerEmail(userEmail)
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setPrice(stripeProps.proPriceId())
                            .setQuantity(1L)
                            .build())
                    .setSuccessUrl(successUrl + "?session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(cancelUrl)
                    .putMetadata("userId", userId.toString())
                    .build();

            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            throw new IllegalStateException("Falha ao criar sessão de pagamento Stripe: " + e.getMessage(), e);
        }
    }

    @Override
    public void cancelSubscription(String stripeSubscriptionId) {
        try {
            com.stripe.model.Subscription subscription =
                    com.stripe.model.Subscription.retrieve(stripeSubscriptionId);
            subscription.cancel();
        } catch (StripeException e) {
            throw new IllegalStateException("Falha ao cancelar subscription no Stripe: " + e.getMessage(), e);
        }
    }
}
