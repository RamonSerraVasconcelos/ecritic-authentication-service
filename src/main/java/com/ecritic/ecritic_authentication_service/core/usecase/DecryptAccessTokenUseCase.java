package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.GenerateUnsignedJwtTokenBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DecryptAccessTokenUseCase {

    private final ValidateAccessTokenUseCase validateAccessTokenUseCase;

    private final GenerateUnsignedJwtTokenBoundary generateUnsignedJwtTokenBoundary;

    public AccessToken execute(String jwToken) {
        log.info("Decrypting access token");

        AccessToken accessToken = validateAccessTokenUseCase.execute(jwToken);

        String unsignedJwtToken = generateUnsignedJwtTokenBoundary.execute(accessToken);
        accessToken.setJwt(unsignedJwtToken);

        return accessToken;
    }
}
