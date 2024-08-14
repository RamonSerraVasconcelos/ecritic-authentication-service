package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.SaveExternalTokenBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.ExternalTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.mapper.ExternalTokenEntityMapper;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.ExternalTokenEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveExternalTokenGateway implements SaveExternalTokenBoundary {

    private final ExternalTokenEntityRepository externalTokenEntityRepository;

    private final ExternalTokenEntityMapper externalTokenEntityMapper;

    public ExternalToken execute(ExternalToken externalToken) {
        ExternalTokenEntity externalTokenEntity = externalTokenEntityMapper.externalTokenToExternalTokenEntity(externalToken);

        ExternalTokenEntity savedExternalTokenEntity = externalTokenEntityRepository.save(externalTokenEntity);

        return externalTokenEntityMapper.externalTokenEntityToExternalToken(savedExternalTokenEntity);
    }
}
