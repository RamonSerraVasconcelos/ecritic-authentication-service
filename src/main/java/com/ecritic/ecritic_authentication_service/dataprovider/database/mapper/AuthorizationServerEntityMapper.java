package com.ecritic.ecritic_authentication_service.dataprovider.database.mapper;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.AuthorizationServerEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorizationServerEntityMapper {

    AuthorizationServer authorizationServerEntityToAuthorizationServer(AuthorizationServerEntity authorizationServerEntity);
}
