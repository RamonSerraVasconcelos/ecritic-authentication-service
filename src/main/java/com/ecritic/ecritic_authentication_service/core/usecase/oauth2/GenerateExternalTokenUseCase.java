package com.ecritic.ecritic_authentication_service.core.usecase.oauth2;

import com.ecritic.ecritic_authentication_service.core.model.AuthorizationRequest;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationServer;
import com.ecritic.ecritic_authentication_service.core.model.ExternalToken;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.oauth2.GenerateExternalTokenBoundary;
import com.ecritic.ecritic_authentication_service.exception.DefaultException;
import com.ecritic.ecritic_authentication_service.exception.InternalErrorException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerateExternalTokenUseCase {

    private final GenerateExternalTokenBoundary generateExternalTokenBoundary;

    public ExternalToken execute(AuthorizationRequest authorizationRequest, AuthorizationServer authorizationServer, String code) {
        log.info("Generating external token for clientId: [{}]", authorizationRequest.getClientId());

        try {
            return generateExternalTokenBoundary.execute(authorizationRequest, authorizationServer, code);
        } catch (DefaultException ex) {
            log.error("Error generating external token for clientId: [{}]. Error: [{}]", authorizationRequest.getClientId(), ex.getErrorResponse());
            throw ex;
        } catch (Exception ex) {
            log.error("Error generating external token for clientId: [{}]", authorizationRequest.getClientId(), ex);
            throw new InternalErrorException(ErrorResponseCode.ECRITICAUTH_11);
        }
    }
}
