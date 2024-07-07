package com.ecritic.ecritic_authentication_service.dataprovider.api.impl;

import com.ecritic.ecritic_authentication_service.core.fixture.UserFixture;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.dataprovider.api.client.UserClient;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserEntityResponse;
import com.ecritic.ecritic_authentication_service.dataprovider.api.fixture.UserEntityResponseFixture;
import com.ecritic.ecritic_authentication_service.dataprovider.api.mapper.UserEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindUserGatewayTest {

    @InjectMocks
    private FindUserGateway findUserGateway;

    @Mock
    private UserClient userClient;

    @Mock
    private UserEntityMapper userEntityMapper;

    @Test
    void givenExecution_thenRetrieve_andReturnUserAuthInfo() {
        UserEntityResponse userEntityResponse = UserEntityResponseFixture.load();
        User user = UserFixture.load();

        when(userClient.getUserAuthorizationInfo(user.getEmail(), null)).thenReturn(userEntityResponse);
        when(userEntityMapper.userEntityToUser(userEntityResponse)).thenReturn(user);

        Optional<User> resultUser = findUserGateway.execute(user.getEmail(), null);

        verify(userClient).getUserAuthorizationInfo(user.getEmail(), null);
        verify(userEntityMapper).userEntityToUser(userEntityResponse);

        assertThat(resultUser).isPresent().get().usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void givenExecutionWithUserId_thenCallClientWithString_andReturnUserAuthInfo() {
        UserEntityResponse userEntityResponse = UserEntityResponseFixture.load();
        User user = UserFixture.load();

        when(userClient.getUserAuthorizationInfo(null, user.getId().toString())).thenReturn(userEntityResponse);
        when(userEntityMapper.userEntityToUser(any())).thenReturn(user);

        Optional<User> resultUser = findUserGateway.execute(null, user.getId());

        verify(userClient).getUserAuthorizationInfo(null, user.getId().toString());
        verify(userEntityMapper).userEntityToUser(userEntityResponse);

        assertThat(resultUser).isPresent().get().usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void givenException_thenReturnOptionalEmpty() {
        String email = "email@email.com";

        doThrow(new RuntimeException()).when(userClient).getUserAuthorizationInfo(any(), any());

        Optional<User> resultUser = findUserGateway.execute(email, null);

        verify(userClient).getUserAuthorizationInfo(email, null);
        verifyNoInteractions(userEntityMapper);

        assertThat(resultUser).isNotPresent();
    }
}