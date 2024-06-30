package com.ecritic.ecritic_authentication_service.entrypoint.mapper;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationData;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthorizationResponseData;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationResponseDataMapper {

    private static final String TOKEN_TYPE = "Bearer";

    public AuthorizationResponseData authorizationDataToAuthorizationResponseData(AuthorizationData authorizationData) {
        return AuthorizationResponseData.builder()
                .accessToken(authorizationData.getAccessToken().getJwt())
                .refreshToken(authorizationData.getRefreshToken().getJwt())
                .tokenType(TOKEN_TYPE)
                .expiresIn(authorizationData.getAccessToken().getExpiresAt().getSecond())
                .refreshExpiresIn(authorizationData.getRefreshToken().getExpiresAt().getSecond())
                .build();
    }
}
