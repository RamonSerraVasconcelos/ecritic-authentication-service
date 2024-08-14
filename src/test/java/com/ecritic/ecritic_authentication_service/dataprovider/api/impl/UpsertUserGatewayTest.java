package com.ecritic.ecritic_authentication_service.dataprovider.api.impl;

import com.ecritic.ecritic_authentication_service.core.fixture.UserFixture;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.dataprovider.api.client.UserClient;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.ClientErrorResponse;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserEntityResponse;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.UserRequest;
import com.ecritic.ecritic_authentication_service.dataprovider.api.fixture.UserEntityResponseFixture;
import com.ecritic.ecritic_authentication_service.dataprovider.api.mapper.UserEntityMapper;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.ClientException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpsertUserGatewayTest {

    @InjectMocks
    private UpsertUserGateway upsertUserGateway;

    @Mock
    private UserClient userClient;

    @Mock
    private UserEntityMapper userEntityMapper;

    @Test
    void givenExecution_callUserClient_andReturnUser() {
        UserEntityResponse userEntityResponse = UserEntityResponseFixture.load();
        User user = UserFixture.load();

        when(userClient.upsertUser(any())).thenReturn(userEntityResponse);
        when(userEntityMapper.userEntityToUser(userEntityResponse)).thenReturn(user);

        User result = upsertUserGateway.execute(user.getEmail(), "Jon");

        verify(userClient).upsertUser(any(UserRequest.class));
        verify(userEntityMapper).userEntityToUser(userEntityResponse);

        assertThat(result).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    void givenExecution_whenClientReturnsError_thenThrowBusinessViolationException() {
        ClientErrorResponse clientErrorResponse = ClientErrorResponse.builder()
                .code("ECRITICUSERS-09")
                .message("User not found")
                .detail("The requested user was not found")
                .build();

        doThrow(new ClientException(clientErrorResponse)).when(userClient).upsertUser(any());

        BusinessViolationException ex = assertThrows(BusinessViolationException.class, () -> upsertUserGateway.execute("email@email.com", "Jon"));

        verify(userClient).upsertUser(any(UserRequest.class));
        verifyNoInteractions(userEntityMapper);

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_13.getCode());
        assertThat(ex.getErrorResponse().getMessage()).isEqualTo(ErrorResponseCode.ECRITICAUTH_13.getMessage());
        assertThat(ex.getErrorResponse().getDetail()).isEqualTo(ErrorResponseCode.ECRITICAUTH_13.getDetail());
    }

    @Test
    void givenExecution_whenExceptionOccurs_thenRethrowException() {
        doThrow(new RuntimeException()).when(userClient).upsertUser(any());

        assertThrows(RuntimeException.class, () -> upsertUserGateway.execute("email@email.com", "Jon"));

        verify(userClient).upsertUser(any(UserRequest.class));
        verifyNoInteractions(userEntityMapper);
    }
}