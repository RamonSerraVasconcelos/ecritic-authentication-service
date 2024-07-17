package com.ecritic.ecritic_authentication_service.dataprovider.api.config;

import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignBuilderConfig {

    @Bean
    public Feign.Builder feignBuilder() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder());
    }
}
