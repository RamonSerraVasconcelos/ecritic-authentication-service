package com.ecritic.ecritic_authentication_service.core.usecase;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationProperties;
import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.Token;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.ValidateJwtTokenBoundary;
import com.ecritic.ecritic_authentication_service.exception.DefaultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateAccessTokenUseCase {

    private final ValidateJwtTokenBoundary validateJwtTokenBoundary;

    private final ApplicationProperties applicationProperties;

    public AccessToken execute(String jwtToken) {
        log.info("Validating access token");

        try {
            Token token = validateJwtTokenBoundary.execute(jwtToken, getSecretKey());

            AccessToken accessToken = new AccessToken();
            accessToken.setId(token.getId());
            accessToken.setJwt(token.getJwt());
            accessToken.setIssuer(token.getIssuer());
            accessToken.setAud(token.getAud());
            accessToken.setUser(token.getUser());
            accessToken.setIssuedAt(token.getIssuedAt());
            accessToken.setExpiresAt(token.getExpiresAt());

            return accessToken;
        } catch (DefaultException ex) {
            log.error("Error validating accessToken. Exception: [{}]", ex.getErrorResponse());
            throw ex;
        } catch (Exception ex) {
            log.error("Error validating accessToken", ex);
            throw ex;
        }
    }

    public SecretKey getSecretKey() {
        byte[] secretBytes = Base64.getDecoder().decode(applicationProperties.getJwtSecret());
        return new SecretKeySpec(secretBytes, "HmacSHA256");
    }
}
