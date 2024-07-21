package com.ecritic.ecritic_authentication_service.dataprovider.database.mapper;

import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.ExternalTokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExternalTokenEntityMapper {

    ExternalTokenEntity externalTokenToExternalTokenEntity(ExternalToken externalToken);

    ExternalToken externalTokenEntityToExternalToken(ExternalTokenEntity externalTokenEntity);
}
