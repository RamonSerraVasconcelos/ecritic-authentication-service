package com.ecritic.ecritic_authentication_service.dataprovider.api.client;

import com.ecritic.ecritic_authentication_service.dataprovider.api.entity.ExternalTokenEntity;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.URI;
import java.util.Map;

@FeignClient(value = "${clients.authorization.name}")
public interface AuthorizationClient {

    @PostMapping()
    @Headers("Content-Type: application/x-www-form-urlencoded")
    ExternalTokenEntity generateExternalToken(URI uri, Map<String, ?> formParams);
}
