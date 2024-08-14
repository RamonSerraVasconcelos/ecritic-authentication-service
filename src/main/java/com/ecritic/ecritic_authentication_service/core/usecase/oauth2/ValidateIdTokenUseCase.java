package com.ecritic.ecritic_authentication_service.core.usecase.oauth2;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.model.IdToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindAuthServerJwksBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.ValidateIdTokenBoundary;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.DefaultException;
import com.ecritic.ecritic_authentication_service.exception.InternalErrorException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.nimbusds.jose.jwk.JWKSet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateIdTokenUseCase {

    private final FindAuthServerJwksBoundary findAuthServerJwksBoundary;

    private final ValidateIdTokenBoundary validateIdTokenBoundary;

    public IdToken execute(String idTokenJwt, AuthorizationServer authorizationServer) {
        log.info("Validating id token: [{}]", idTokenJwt);

        try {
            JWKSet jwks = findAuthServerJwksBoundary.execute(authorizationServer.getJwksEndpoint());

            IdToken idToken = validateIdTokenBoundary.execute(idTokenJwt, jwks);

            verifyIdToken(idToken, authorizationServer.getClientId());

            return idToken;
        } catch (DefaultException ex) {
            log.error("Error validating id token: [{}]. Error: [{}]", idTokenJwt, ex.getErrorResponse());
            throw ex;
        } catch (Exception ex) {
            log.error("Error validating id token: [{}].", idTokenJwt, ex);
            throw new InternalErrorException(ErrorResponseCode.ECRITICAUTH_11);
        }
    }

    private void verifyIdToken(IdToken idToken, String clientId) {
        if (idToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            log.warn("Id token expired at: [{}]", idToken.getExpiresAt());
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_12);
        }

        if (idToken.getAud().stream().noneMatch(clientId::equals)) {
            log.warn("Id token audience does not match: [{}]", clientId);
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_12);
        }
    }
}
