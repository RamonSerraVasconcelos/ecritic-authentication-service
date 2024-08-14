package com.ecritic.ecritic_authentication_service.dataprovider.api.config;

import com.ecritic.ecritic_authentication_service.dataprovider.api.interpector.FeignDefaultInterceptor;
import feign.Logger;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class FeignDefaultConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new ClientErrorDecoder();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    public FeignDefaultInterceptor feignDefaultInterceptor() {
        return new FeignDefaultInterceptor();
    }
}
