package com.ecritic.ecritic_authentication_service.exception;

import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.AuthServerErrorResponse;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.ClientErrorResponse;
import lombok.Getter;

@Getter
public class ClientException extends RuntimeException {

    private AuthServerErrorResponse response;
    private ClientErrorResponse clientErrorResponse;

    public ClientException(AuthServerErrorResponse response) {
        this.response = response;
    }

    public ClientException(ClientErrorResponse clientErrorResponse) {
        this.clientErrorResponse = clientErrorResponse;
    }
}
