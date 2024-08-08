package com.ecritic.ecritic_authentication_service.dataprovider.database.mapper;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.AuthorizationServerEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.fixture.AuthorizationServerEntityFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

class AuthorizationServerEntityMapperTest {

    private AuthorizationServerEntityMapper authorizationServerEntityMapper;

    @BeforeEach
    public void setUp() {
        authorizationServerEntityMapper = Mappers.getMapper(AuthorizationServerEntityMapper.class);
    }

    @Test
    void givenAuthorizationServerEntity_thenReturnAuthorizationServer() {
        AuthorizationServerEntity authorizationServerEntity = AuthorizationServerEntityFixture.load();

        AuthorizationServer authorizationServer = authorizationServerEntityMapper.authorizationServerEntityToAuthorizationServer(authorizationServerEntity);

        assertThat(authorizationServer).isNotNull().usingRecursiveComparison().isEqualTo(authorizationServerEntity);
    }
}