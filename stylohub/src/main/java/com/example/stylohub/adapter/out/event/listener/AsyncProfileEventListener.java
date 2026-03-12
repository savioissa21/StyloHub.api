package com.example.stylohub.adapter.out.event.listener;

import com.example.stylohub.adapter.in.web.cache.ProfileCacheService;
import com.example.stylohub.application.port.out.EmailPort;
import com.example.stylohub.application.port.out.ProfileRepositoryPort;
import com.example.stylohub.domain.event.ThemeUpdatedEvent;
import com.example.stylohub.domain.event.UserCreatedEvent;
import com.example.stylohub.domain.event.WidgetAddedEvent;
import com.example.stylohub.domain.event.WidgetRemovedEvent;
import com.example.stylohub.domain.model.OAuthProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncProfileEventListener {

    private static final Logger log = LoggerFactory.getLogger(AsyncProfileEventListener.class);

    private final ProfileCacheService profileCacheService;
    private final ProfileRepositoryPort profileRepo;
    private final EmailPort emailPort;

    public AsyncProfileEventListener(ProfileCacheService profileCacheService,
                                     ProfileRepositoryPort profileRepo,
                                     EmailPort emailPort) {
        this.profileCacheService = profileCacheService;
        this.profileRepo = profileRepo;
        this.emailPort = emailPort;
    }

    @Async
    @EventListener
    public void onThemeUpdated(ThemeUpdatedEvent event) {
        log.info("[EVENT] ThemeUpdated - profileId={}, username={}", event.profileId(), event.username());
        profileCacheService.evict(event.username());
    }

    @Async
    @EventListener
    public void onWidgetAdded(WidgetAddedEvent event) {
        log.info("[EVENT] WidgetAdded - profileId={}, widgetId={}, type={}",
                event.profileId(), event.widgetId(), event.widgetType());
        profileCacheService.evict(event.username());
    }

    @Async
    @EventListener
    public void onWidgetRemoved(WidgetRemovedEvent event) {
        log.info("[EVENT] WidgetRemoved - profileId={}, widgetId={}", event.profileId(), event.widgetId());
        profileCacheService.evict(event.username());
    }

    @Async
    @EventListener
    public void onUserCreated(UserCreatedEvent event) {
        log.info("[EVENT] UserCreated - userId={}, email={}, provider={}",
                event.userId(), event.email(), event.provider());

        // Não envia e-mail de boas-vindas para logins OAuth (Google, GitHub, etc.)
        if (event.provider() != OAuthProvider.LOCAL) {
            return;
        }

        profileRepo.findByUserId(event.userId()).ifPresent(profile ->
            emailPort.sendWelcome(event.email(), profile.getUsername())
        );
    }
}
