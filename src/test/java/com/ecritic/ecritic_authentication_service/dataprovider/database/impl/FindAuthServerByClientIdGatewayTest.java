package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.AuthorizationServerEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.mapper.AuthorizationServerEntityMapper;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.AuthorizationServerEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindAuthServerByClientIdGatewayTest {

    @InjectMocks
    private FindAuthServerByClientIdGateway findAuthServerByClientIdGateway;

    @Mock
    private AuthorizationServerEntityRepository authorizationServerEntityRepository;

    @Mock
    private AuthorizationServerEntityMapper authorizationServerEntityMapper;

    @Test
    void givenValidClientId_thenReturnsAuthorizationServer() {
        String clientId = "valid-client-id";
        AuthorizationServerEntity entity = new AuthorizationServerEntity();
        AuthorizationServer authorizationServer = new AuthorizationServer();

        when(authorizationServerEntityRepository.findByClientId(clientId)).thenReturn(entity);
        when(authorizationServerEntityMapper.authorizationServerEntityToAuthorizationServer(entity)).thenReturn(authorizationServer);

        Optional<AuthorizationServer> result = findAuthServerByClientIdGateway.execute(clientId);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorizationServer);
    }

    @Test
    void givenInvalidClientId_thenReturnsEmptyOptional() {
        String clientId = "invalid-client-id";

        when(authorizationServerEntityRepository.findByClientId(clientId)).thenReturn(null);

        Optional<AuthorizationServer> result = findAuthServerByClientIdGateway.execute(clientId);

        assertThat(result).isNotPresent();
    }
}