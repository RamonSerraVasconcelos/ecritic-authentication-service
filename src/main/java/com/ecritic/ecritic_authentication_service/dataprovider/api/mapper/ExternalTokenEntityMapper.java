package com.ecritic.ecritic_authentication_service.dataprovider.api.mapper;

import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.ExternalTokenEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExternalTokenEntityMapper {

    ExternalToken externalTokenEntityToExternalToken(ExternalTokenEntity externalTokenEntity);
}
