package com.ecritic.ecritic_authentication_service.dataprovider.api.config;

import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.AuthServerErrorResponse;
import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.ClientErrorResponse;
import com.ecritic.ecritic_authentication_service.exception.ClientException;
import com.ecritic.ecritic_authentication_service.exception.InternalErrorException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class ClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ClientErrorResponse clientErrorResponse = objectMapper.readValue(response.body().asInputStream(), ClientErrorResponse.class);

            return new ClientException(clientErrorResponse);
        } catch (IOException ex) {
            log.error("Error decoding response from api client", ex);
            throw new InternalErrorException(ErrorResponseCode.ECRITICAUTH_13);
        }
    }
}
