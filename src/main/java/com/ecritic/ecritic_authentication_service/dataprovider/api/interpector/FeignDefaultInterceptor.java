package com.ecritic.ecritic_authentication_service.dataprovider.api.interpector;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationRequestHeaders;
import com.ecritic.ecritic_authentication_service.config.properties.ThreadRequestProperties;
import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignDefaultInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        template.header(ApplicationRequestHeaders.REQUEST_ID.getValue(), ThreadRequestProperties.getRequestId());
    }
}
