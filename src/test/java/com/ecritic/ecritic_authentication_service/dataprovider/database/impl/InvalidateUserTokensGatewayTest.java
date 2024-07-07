package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.RefreshTokenEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class InvalidateUserTokensGatewayTest {

    @InjectMocks
    private InvalidateUserTokensGateway invalidateUserTokensGateway;

    @Mock
    private RefreshTokenEntityRepository refreshTokenEntityRepository;

    @Test
    void givenExecution_thenInvalidateUserTokens() {
        UUID userId = UUID.randomUUID();

        invalidateUserTokensGateway.execute(userId);

        verify(refreshTokenEntityRepository).invalidateByUserId(userId);
    }
}