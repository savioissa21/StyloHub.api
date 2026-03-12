package com.example.stylohub.adapter.out.event.listener;

import com.example.stylohub.domain.event.ThemeUpdatedEvent;
import com.example.stylohub.domain.event.UserCreatedEvent;
import com.example.stylohub.domain.event.WidgetAddedEvent;
import com.example.stylohub.domain.event.WidgetRemovedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncProfileEventListener {

    private static final Logger log = LoggerFactory.getLogger(AsyncProfileEventListener.class);

    @Async
    @EventListener
    public void onThemeUpdated(ThemeUpdatedEvent event) {
        log.info("[EVENT] ThemeUpdated - profileId={}, username={}", event.profileId(), event.username());
        // TODO: Invalidar cache CDN (Cloudflare/Edge) para /{username}
    }

    @Async
    @EventListener
    public void onWidgetAdded(WidgetAddedEvent event) {
        log.info("[EVENT] WidgetAdded - profileId={}, widgetId={}, type={}",
                event.profileId(), event.widgetId(), event.widgetType());
        // TODO: Invalidar cache CDN
    }

    @Async
    @EventListener
    public void onWidgetRemoved(WidgetRemovedEvent event) {
        log.info("[EVENT] WidgetRemoved - profileId={}, widgetId={}", event.profileId(), event.widgetId());
        // TODO: Invalidar cache CDN
    }

    @Async
    @EventListener
    public void onUserCreated(UserCreatedEvent event) {
        log.info("[EVENT] UserCreated - userId={}, email={}, provider={}",
                event.userId(), event.email(), event.provider());
        // TODO: Enviar e-mail de boas-vindas
    }
}
