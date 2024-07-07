package com.ecritic.ecritic_authentication_service.dataprovider.api.mapper;

import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserEntityResponse;
import com.ecritic.ecritic_authentication_service.dataprovider.api.fixture.UserEntityResponseFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class UserEntityMapperTest {

    private UserEntityMapper userEntityMapper;

    @BeforeEach
    public void setUp() {
        userEntityMapper = Mappers.getMapper(UserEntityMapper.class);
    }

    @Test
    void givenUserEntity_thenReturnUser() {
        UserEntityResponse userEntityResponse = UserEntityResponseFixture.load();

        User user = userEntityMapper.userEntityToUser(userEntityResponse);

        assertThat(user).isNotNull();
        assertThat(user.getId().toString()).isEqualTo(userEntityResponse.getId());
        assertThat(user.getEmail()).isEqualTo(userEntityResponse.getEmail());
        assertThat(user.getPassword()).isEqualTo(userEntityResponse.getPassword());
        assertThat(user.getRole().name()).isEqualTo(userEntityResponse.getRole());
        assertThat(user.isActive()).isEqualTo(userEntityResponse.isActive());
    }
}