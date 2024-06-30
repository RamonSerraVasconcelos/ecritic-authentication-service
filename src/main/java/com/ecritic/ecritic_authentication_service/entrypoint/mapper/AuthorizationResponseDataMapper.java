package com.ecritic.ecritic_authentication_service.entrypoint.mapper;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationData;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthorizationResponseData;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;

@Component
public class AuthorizationResponseDataMapper {

    private static final String TOKEN_TYPE = "Bearer";

    public AuthorizationResponseData authorizationDataToAuthorizationResponseData(AuthorizationData authorizationData) {
        return AuthorizationResponseData.builder()
                .accessToken(authorizationData.getAccessToken().getJwt())
                .refreshToken(authorizationData.getRefreshToken().getJwt())
                .tokenType(TOKEN_TYPE)
                .expiresIn((int) authorizationData.getAccessToken().getExpiresAt().toEpochSecond(ZoneOffset.UTC))
                .refreshExpiresIn((int) authorizationData.getRefreshToken().getExpiresAt().toEpochSecond(ZoneOffset.UTC))
                .build();
    }
}
