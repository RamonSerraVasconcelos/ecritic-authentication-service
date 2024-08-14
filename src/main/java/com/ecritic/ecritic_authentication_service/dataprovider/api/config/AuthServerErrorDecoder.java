package com.ecritic.ecritic_authentication_service.dataprovider.api.config;

import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.AuthServerErrorResponse;
import com.ecritic.ecritic_authentication_service.exception.ClientException;
import com.ecritic.ecritic_authentication_service.exception.InternalErrorException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class AuthServerErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            AuthServerErrorResponse authServerErrorResponse = objectMapper.readValue(response.body().asInputStream(), AuthServerErrorResponse.class);

            return new ClientException(authServerErrorResponse);
        } catch (IOException ex) {
            log.error("Error decoding response from api client", ex);
            throw new InternalErrorException(ErrorResponseCode.ECRITICAUTH_13);
        }
    }
}
