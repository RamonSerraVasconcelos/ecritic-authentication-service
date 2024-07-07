package com.ecritic.ecritic_authentication_service.entrypoint.mapper;

import com.ecritic.ecritic_authentication_service.core.fixture.AuthorizationDataFixture;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationData;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthorizationResponseData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorizationResponseDataMapperTest {

    private AuthorizationResponseDataMapper authorizationResponseDataMapper;

    @BeforeEach
    public void setUp() {
        authorizationResponseDataMapper = new AuthorizationResponseDataMapper();
    }

    @Test
    void givenAuthorizationData_thenReturnAuthorizationResponseData() {
        AuthorizationData authorizationData = AuthorizationDataFixture.load();

        AuthorizationResponseData authorizationResponseData = authorizationResponseDataMapper.authorizationDataToAuthorizationResponseData(authorizationData);

        assertThat(authorizationResponseData.getAccessToken()).isEqualTo(authorizationData.getAccessToken().getJwt());
        assertThat(authorizationResponseData.getRefreshToken()).isEqualTo(authorizationData.getRefreshToken().getJwt());
        assertThat(authorizationResponseData.getTokenType()).isEqualTo("Bearer");
        assertThat(authorizationResponseData.getExpiresIn()).isEqualTo((int) authorizationData.getAccessToken().getExpiresAt().toEpochSecond(ZoneOffset.UTC));
        assertThat(authorizationResponseData.getRefreshExpiresIn()).isEqualTo((int) authorizationData.getRefreshToken().getExpiresAt().toEpochSecond(ZoneOffset.UTC));
    }
}