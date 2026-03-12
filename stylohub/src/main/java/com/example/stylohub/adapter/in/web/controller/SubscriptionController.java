package com.example.stylohub.adapter.in.web.controller;

import com.example.stylohub.application.port.in.ManageProfileUseCase;
import com.example.stylohub.application.port.out.SubscriptionPaymentPort;
import com.example.stylohub.infrastructure.security.StyloHubUserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/creator/subscription")
@Tag(name = "Subscription", description = "Gestão de planos e pagamentos")
@SecurityRequirement(name = "bearerAuth")
public class SubscriptionController {

    private final SubscriptionPaymentPort paymentPort;
    private final ManageProfileUseCase profileUseCase;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public SubscriptionController(SubscriptionPaymentPort paymentPort,
                                  ManageProfileUseCase profileUseCase) {
        this.paymentPort = paymentPort;
        this.profileUseCase = profileUseCase;
    }

    @PostMapping("/checkout")
    @Operation(summary = "Cria uma sessão de checkout Stripe para upgrade para PRO")
    Map<String, String> createCheckout(@AuthenticationPrincipal StyloHubUserPrincipal principal) {
        var profile = profileUseCase.getProfileByUserId(principal.getUserIdAsUUID());
        String checkoutUrl = paymentPort.createCheckoutSession(
                principal.getUserIdAsUUID(),
                principal.email(),
                frontendUrl + "/dashboard?upgrade=success",
                frontendUrl + "/dashboard?upgrade=canceled"
        );
        return Map.of("checkoutUrl", checkoutUrl);
    }
}
