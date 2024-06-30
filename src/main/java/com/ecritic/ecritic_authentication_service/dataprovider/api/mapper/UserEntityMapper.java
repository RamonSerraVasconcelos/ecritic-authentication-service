package com.ecritic.ecritic_authentication_service.dataprovider.api.mapper;

import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    User userEntityToUser(UserEntity userEntity);
}
