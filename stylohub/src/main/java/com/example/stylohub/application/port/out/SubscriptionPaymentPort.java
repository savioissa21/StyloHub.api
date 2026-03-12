package com.example.stylohub.application.port.out;

import java.util.UUID;

public interface SubscriptionPaymentPort {
    /**
     * Cria uma sessão de checkout no Stripe para upgrade para PRO.
     * @return URL da sessão de pagamento do Stripe
     */
    String createCheckoutSession(UUID userId, String userEmail, String successUrl, String cancelUrl);

    /**
     * Cancela a subscription ativa do utilizador no Stripe.
     */
    void cancelSubscription(String stripeSubscriptionId);
}
