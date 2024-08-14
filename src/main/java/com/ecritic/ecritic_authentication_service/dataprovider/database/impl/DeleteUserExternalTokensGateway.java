package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.DeleteUserExternalTokensBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.ExternalTokenEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteUserExternalTokensGateway implements DeleteUserExternalTokensBoundary {

    private final ExternalTokenEntityRepository externalTokenEntityRepository;

    public void execute(UUID userId, String clientId, UUID currentTokenId) {
        log.info("Deleting external tokens for userId: [{}] and clientId: [{}]", userId, clientId);

        try {
            externalTokenEntityRepository.deleteUserTokens(userId, clientId, currentTokenId);
        } catch (Exception ex) {
            log.error("Error deleting external tokens for userId: [{}] and clientId: [{}]", userId, clientId, ex);
        }
    }
}
