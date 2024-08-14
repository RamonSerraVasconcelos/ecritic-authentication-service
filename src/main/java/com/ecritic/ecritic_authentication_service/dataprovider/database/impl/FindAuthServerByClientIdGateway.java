package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindAuthServerByClientIdBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.AuthorizationServerEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.mapper.AuthorizationServerEntityMapper;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.AuthorizationServerEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindAuthServerByClientIdGateway implements FindAuthServerByClientIdBoundary {

    private final AuthorizationServerEntityRepository authorizationServerEntityRepository;

    private final AuthorizationServerEntityMapper authorizationServerEntityMapper;

    public Optional<AuthorizationServer> execute(String clientId) {
        AuthorizationServerEntity authorizationServerEntity = authorizationServerEntityRepository.findByClientId(clientId);

        AuthorizationServer authorizationServer = authorizationServerEntityMapper.authorizationServerEntityToAuthorizationServer(authorizationServerEntity);

        return Optional.ofNullable(authorizationServer);
    }
}
