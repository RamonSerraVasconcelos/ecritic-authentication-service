package com.ecritic.ecritic_authentication_service.entrypoint.mapper;

import com.ecritic.ecritic_authentication_service.core.model.AuthenticationData;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthenticationResponseData;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class AuthenticationResponseDataMapper {

    private static final String TOKEN_TYPE = "Bearer";

    public AuthenticationResponseData authorizationDataToAuthorizationResponseData(AuthenticationData authenticationData) {
        return AuthenticationResponseData.builder()
                .accessToken(authenticationData.getAccessToken().getJwt())
                .refreshToken(authenticationData.getRefreshToken().getJwt())
                .tokenType(TOKEN_TYPE)
                .expiresIn((int) authenticationData.getAccessToken().getExpiresAt().toEpochSecond(ZoneOffset.UTC))
                .refreshExpiresIn((int) authenticationData.getRefreshToken().getExpiresAt().toEpochSecond(ZoneOffset.UTC))
                .build();
    }
}
