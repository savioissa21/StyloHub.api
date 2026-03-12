package com.example.stylohub.adapter.in.web.cache;

import com.example.stylohub.adapter.in.web.dto.ProfileResponse;
import com.example.stylohub.adapter.in.web.mapper.WebProfileMapper;
import com.example.stylohub.application.port.in.ManageProfileUseCase;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Wrapper cacheable para perfis públicos.
 * Mantém o domínio limpo e centraliza a lógica de cache na camada de adapter.
 */
@Component
public class ProfileCacheService {

    private final ManageProfileUseCase profileUseCase;
    private final WebProfileMapper mapper;

    public ProfileCacheService(ManageProfileUseCase profileUseCase, WebProfileMapper mapper) {
        this.profileUseCase = profileUseCase;
        this.mapper = mapper;
    }

    @Cacheable(value = "profiles", key = "#username")
    public ProfileResponse getPublicProfile(String username) {
        return mapper.toPublicResponse(profileUseCase.getProfileByUsername(username));
    }

    @CacheEvict(value = "profiles", key = "#username")
    public void evict(String username) {
        // Apenas evicta a entrada do cache — chamado pelo listener de eventos
    }
}
