package com.ecritic.ecritic_authentication_service.dataprovider.api.config;

import com.ecritic.ecritic_authentication_service.dataprovider.api.interpector.FeignDefaultInterceptor;
import org.springframework.context.annotation.Bean;

import feign.Logger;

public class FeignDefaultConfig {

    @Bean
    public FeignDefaultInterceptor feignDefaultInterceptor() {
        return new FeignDefaultInterceptor();
    }

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
