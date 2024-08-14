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
class FindAuthServerByNameGatewayTest {

    @InjectMocks
    private FindAuthServerByNameGateway findAuthServerByNameGateway;

    @Mock
    private AuthorizationServerEntityRepository authorizationServerRepository;

    @Mock
    private AuthorizationServerEntityMapper authorizationServerEntityMapper;

    @Test
    void givenValidName_thenReturnsAuthorizationServer() {
        String name = "valid-name";
        AuthorizationServerEntity entity = new AuthorizationServerEntity();
        AuthorizationServer authorizationServer = new AuthorizationServer();

        when(authorizationServerRepository.findByName(name)).thenReturn(entity);
        when(authorizationServerEntityMapper.authorizationServerEntityToAuthorizationServer(entity)).thenReturn(authorizationServer);

        Optional<AuthorizationServer> result = findAuthServerByNameGateway.execute(name);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(authorizationServer);
    }

    @Test
    void givenInvalidName_thenReturnsEmptyOptional() {
        String name = "invalid-name";

        when(authorizationServerRepository.findByName(name)).thenReturn(null);

        Optional<AuthorizationServer> result = findAuthServerByNameGateway.execute(name);

        assertThat(result).isNotPresent();
    }
}