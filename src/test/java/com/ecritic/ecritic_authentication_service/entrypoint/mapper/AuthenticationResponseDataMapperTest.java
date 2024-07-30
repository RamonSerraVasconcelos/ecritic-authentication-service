package com.ecritic.ecritic_authentication_service.entrypoint.mapper;

import com.ecritic.ecritic_authentication_service.core.fixture.AuthenticationDataFixture;
import com.ecritic.ecritic_authentication_service.core.model.AuthenticationData;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthenticationResponseData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationResponseDataMapperTest {

    private AuthenticationResponseDataMapper authenticationResponseDataMapper;

    @BeforeEach
    public void setUp() {
        authenticationResponseDataMapper = new AuthenticationResponseDataMapper();
    }

    @Test
    void givenAuthorizationData_thenReturnAuthorizationResponseData() {
        AuthenticationData authenticationData = AuthenticationDataFixture.load();

        AuthenticationResponseData authenticationResponseData = authenticationResponseDataMapper.authorizationDataToAuthorizationResponseData(authenticationData);

        assertThat(authenticationResponseData.getAccessToken()).isEqualTo(authenticationData.getAccessToken().getJwt());
        assertThat(authenticationResponseData.getRefreshToken()).isEqualTo(authenticationData.getRefreshToken().getJwt());
        assertThat(authenticationResponseData.getTokenType()).isEqualTo("Bearer");
        assertThat(authenticationResponseData.getExpiresIn()).isEqualTo((int) authenticationData.getAccessToken().getExpiresAt().toEpochSecond(ZoneOffset.UTC));
        assertThat(authenticationResponseData.getRefreshExpiresIn()).isEqualTo((int) authenticationData.getRefreshToken().getExpiresAt().toEpochSecond(ZoneOffset.UTC));
    }
}