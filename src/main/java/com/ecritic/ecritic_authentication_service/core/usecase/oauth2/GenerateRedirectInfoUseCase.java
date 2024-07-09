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
            AuthorizationServer authorizationServer = getAuthServer(authServerName);

            if (!authorizationServer.getRedirectUris().contains(redirectUri)) {
                log.error("Redirect URI [{}] not allowed for auth server [{}]", redirectUri, authServerName);
                throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_10);
            }

            String state = generateRandomState();

            String url = buildBaseUri(authorizationServer, redirectUri.toString(), state);

            cacheStateBoundary.execute(state, authorizationServer.getClientId());

            log.info("Generated redirect info with clientId: [{}]", authorizationServer.getClientId());

            return URI.create(url);
        } catch (DefaultException ex) {
            log.error("Error generating redirect info for auth server [{}]. Error: [{}]", authServerName, ex.getErrorResponse());
            throw ex;
        } catch (Exception ex) {
            log.error("Error generating redirect info for auth server [{}]", authServerName, ex);
            throw new InternalErrorException(ErrorResponseCode.ECRITICAUTH_11);
        }
    }

    private AuthorizationServer getAuthServer(String name) {
        Optional<AuthorizationServer> optionalAuthServer = findAuthServerByNameBoundary.execute(name);

        if (optionalAuthServer.isEmpty()) {
            log.warn("Auth server {} not found", name);
            throw new EntityNotFoundException(ErrorResponseCode.ECRITICAUTH_09);
        }

        return optionalAuthServer.get();
    }

    private String buildBaseUri(AuthorizationServer authorizationServer, String redirectUri, String state) {
        String url = authorizationServer.getAuthEndpoint() +
                "?client_id=" + authorizationServer.getClientId() +
                "&response_type=" + authorizationServer.getResponseType() +
                "&redirect_uri=" + redirectUri +
                "&scope=" + authorizationServer.getFormattedScopes() +
                "&state=" + state;

        if (nonNull(authorizationServer.getAdditionalParams()) && !authorizationServer.getAdditionalParams().isEmpty()) {
            log.info("Adding additional params in request");

            StringBuilder urlBuilder = new StringBuilder(url);

            authorizationServer.getAdditionalParams().forEach((key, value) -> {
                urlBuilder.append("&").append(key).append("=").append(value);
            });

            url = urlBuilder.toString();
        }

        return url;
    }

    private String generateRandomState() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        return randomGenerator.ints(10, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
}
