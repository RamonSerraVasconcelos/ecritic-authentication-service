package com.ecritic.ecritic_authentication_service.core.usecase.oauth2;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;
import com.ecritic.ecritic_authentication_service.core.model.IdToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindAuthServerByClientIdBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.FindStateBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.UpsertUserBoundary;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.DefaultException;
import com.ecritic.ecritic_authentication_service.exception.InternalErrorException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateCallbackUseCase {

    private final FindStateBoundary findCachedStateBoundary;

    private final FindAuthServerByClientIdBoundary findAuthServerByClientIdBoundary;

    private final GenerateExternalTokenUseCase generateExternalTokenUseCase;

    private final ValidateIdTokenUseCase validateIdTokenUseCase;

    private final UpsertUserBoundary upsertUserBoundary;

    public void execute(String code, String state, String error, String errorDescription) {
        log.info("Validating authorization callback with state: [{}]", state);

        try {
            checkForErrors(error, errorDescription);

            Optional<AuthorizationRequest> authorizationRequestOptional = findCachedStateBoundary.execute(state);

            if (authorizationRequestOptional.isEmpty()) {
                log.warn("State provided by callback is invalid");
                throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_12);
            }

            AuthorizationRequest authorizationRequest = authorizationRequestOptional.get();

            Optional<AuthorizationServer> authorizationServer = findAuthServerByClientIdBoundary.execute(authorizationRequest.getClientId());

            if (authorizationServer.isEmpty()) {
                log.error("Authorization server not found for client id: [{}]", authorizationRequest.getClientId());
                throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_12);
            }

            ExternalToken externalToken = generateExternalTokenUseCase.execute(authorizationRequest, authorizationServer.get(), code);

            IdToken idToken = validateIdTokenUseCase.execute(externalToken.getIdToken(), authorizationServer.get());

            User user = upsertUserBoundary.execute(idToken.getEmail(), idToken.getName());
        } catch (DefaultException ex) {
            log.error("Error validating authorization callback with state: [{}]. Error: [{}]", state, ex.getErrorResponse());
            throw ex;
        } catch (Exception ex) {
            log.error("Error validating authorization callback with state: [{}]", state, ex);
            throw new InternalErrorException(ErrorResponseCode.ECRITICAUTH_11);
        }
    }

    private void checkForErrors(String error, String errorDescription) {
        if (error != null || errorDescription != null) {
            log.warn("Authorization server callback returned error: [{}] and error description: [{}]", error, errorDescription);
            throw new BusinessViolationException(ErrorResponseCode.ECRITICAUTH_14);
        }
    }
}
