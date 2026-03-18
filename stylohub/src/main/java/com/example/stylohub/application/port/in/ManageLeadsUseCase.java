package com.example.stylohub.application.port.in;

import com.example.stylohub.domain.model.Lead;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ManageLeadsUseCase {
    /** Captura um lead submetido por um visitante na página pública. */
    Lead captureLead(String username, UUID widgetId, Map<String, String> fields);

    /** Lista todos os leads do perfil (ordenados por data desc). */
    List<Lead> getLeadsByProfileId(UUID profileId);

    /** Remove um lead (só o dono do perfil pode chamar este método). */
    void deleteLead(UUID profileId, UUID leadId);
}
