package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.ExternalTokenEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeleteUserExternalTokensGatewayTest {

    @InjectMocks
    private DeleteUserExternalTokensGateway deleteUserExternalTokensGateway;

    @Mock
    private ExternalTokenEntityRepository externalTokenEntityRepository;

    @Test
    void givenExecution_thenDeletesUserTokens() {
        UUID userId = UUID.randomUUID();
        String clientId = "client-id";
        UUID currentTokenId = UUID.randomUUID();

        deleteUserExternalTokensGateway.execute(userId, clientId, currentTokenId);

        verify(externalTokenEntityRepository).deleteUserTokens(userId, clientId, currentTokenId);
    }

    @Test
    void givenException_thenLogError_andDontThrowException() {
        UUID userId = UUID.randomUUID();
        String clientId = "client-id";
        UUID currentTokenId = UUID.randomUUID();

        doThrow(new RuntimeException("Test Exception")).when(externalTokenEntityRepository).deleteUserTokens(userId, clientId, currentTokenId);

        deleteUserExternalTokensGateway.execute(userId, clientId, currentTokenId);

        verify(externalTokenEntityRepository).deleteUserTokens(userId, clientId, currentTokenId);
    }
}