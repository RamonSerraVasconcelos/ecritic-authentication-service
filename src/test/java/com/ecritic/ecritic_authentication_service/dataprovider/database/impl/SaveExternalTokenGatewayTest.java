package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.ExternalTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.mapper.ExternalTokenEntityMapper;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.ExternalTokenEntityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveExternalTokenGatewayTest {

    @InjectMocks
    private SaveExternalTokenGateway saveExternalTokenGateway;

    @Mock
    private ExternalTokenEntityRepository externalTokenEntityRepository;

    @Mock
    private ExternalTokenEntityMapper externalTokenEntityMapper;

    @Test
    void givenExternalToken_thenSavesAndReturnsExternalToken() {
        ExternalToken externalToken = new ExternalToken();
        ExternalTokenEntity externalTokenEntity = new ExternalTokenEntity();
        ExternalTokenEntity savedExternalTokenEntity = new ExternalTokenEntity();
        ExternalToken savedExternalToken = new ExternalToken();

        when(externalTokenEntityMapper.externalTokenToExternalTokenEntity(externalToken)).thenReturn(externalTokenEntity);
        when(externalTokenEntityRepository.save(externalTokenEntity)).thenReturn(savedExternalTokenEntity);
        when(externalTokenEntityMapper.externalTokenEntityToExternalToken(savedExternalTokenEntity)).thenReturn(savedExternalToken);

        ExternalToken result = saveExternalTokenGateway.execute(externalToken);

        assertThat(result).isEqualTo(savedExternalToken);
    }
}