package com.ecritic.ecritic_authentication_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableFeignClients
@EnableMongoRepositories
public class ECriticAuthenticationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECriticAuthenticationServiceApplication.class, args);
    }

}
