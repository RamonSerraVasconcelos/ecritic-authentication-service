package com.ecritic.ecritic_authentication_service.dataprovider.database.impl;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindAuthServerByNameBoundary;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.AuthorizationServerEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.mapper.AuthorizationServerEntityMapper;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.AuthorizationServerEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindAuthServerByNameGateway implements FindAuthServerByNameBoundary {

    private final AuthorizationServerEntityRepository authorizationServerRepository;

    private final AuthorizationServerEntityMapper authorizationServerEntityMapper;

    public Optional<AuthorizationServer> execute(String name) {
        AuthorizationServerEntity authorizationServerEntity = authorizationServerRepository.findByName(name);

        AuthorizationServer authorizationServer = authorizationServerEntityMapper.authorizationServerEntityToAuthorizationServer(authorizationServerEntity);

        return Optional.ofNullable(authorizationServer);
    }
}
