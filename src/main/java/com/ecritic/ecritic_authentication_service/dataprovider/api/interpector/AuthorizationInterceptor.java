package com.ecritic.ecritic_authentication_service.dataprovider.api.interpector;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public class AuthorizationInterceptor implements RequestInterceptor {

    private final Supplier<String> urlSupplier;

    @Override
    public void apply(RequestTemplate template) {
        template.target(urlSupplier.get());
    }
}
