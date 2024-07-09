package com.ecritic.ecritic_authentication_service.core.usecase.oauth2;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.CacheStateBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindAuthServerByNameBoundary;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.DefaultException;
import com.ecritic.ecritic_authentication_service.exception.EntityNotFoundException;
import com.ecritic.ecritic_authentication_service.exception.InternalErrorException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.security.SecureRandom;
import java.util.Optional;
import java.util.random.RandomGenerator;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerateRedirectInfoUseCase {

    private final FindAuthServerByNameBoundary findAuthServerByNameBoundary;

    private final CacheStateBoundary cacheStateBoundary;

    private final RandomGenerator randomGenerator = new SecureRandom();

    public URI execute(String authServerName, URI redirectUri) {
        log.info("Generating redirect info for auth server [{}]", authServerName);

        try {
            Optional<AuthorizationServer> optionalAuthServer = findAuthServerByNameBoundary.execute(authServerName);

            if (optionalAuthServer.isEmpty()) {
                log.warn("Auth server {} not found", authServerName);
                throw new EntityNotFoundException(ErrorResponseCode.ECRITICAUTH_09);
            }

            AuthorizationServer authServer = optionalAuthServer.get();

            if (!authServer.getRedirectUris().contains(redirectUri)) {
                log.error("Redirect URI [{}] not allowed for auth server [{}]", redirectUri, authServerName);
                throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_10);
            }

            String state = generateRandomState();

            String url = authServer.getAuthEndpoint() +
                    "?client_id=" + authServer.getClientId() +
                    "&response_type=" + authServer.getResponseType() +
                    "&redirect_uri=" + redirectUri +
                    "&scope=" + authServer.getFormattedScopes() +
                    "&state=" + state;

            if (nonNull(authServer.getAdditionalParams()) && !authServer.getAdditionalParams().isEmpty()) {
                log.info("Adding additional params in request");

                StringBuilder urlBuilder = new StringBuilder(url);

                authServer.getAdditionalParams().forEach((key, value) -> {
                    urlBuilder.append("&").append(key).append("=").append(value);
                });

                url = urlBuilder.toString();
            }

            cacheStateBoundary.execute(state, authServer.getClientId());

            log.info("Generated redirect info with clientId: [{}]", authServer.getClientId());

            return URI.create(url);
        } catch (DefaultException ex) {
            log.error("Error generating redirect info for auth server [{}]. Error: [{}]", authServerName, ex.getErrorResponse());
            throw ex;
        } catch (Exception ex) {
            log.error("Error generating redirect info for auth server [{}]", authServerName, ex);
            throw new InternalErrorException(ErrorResponseCode.ECRITICAUTH_11);
        }
    }

    private String generateRandomState() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        return randomGenerator.ints(10, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
