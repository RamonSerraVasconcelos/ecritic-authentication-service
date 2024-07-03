package com.ecritic.ecritic_authentication_service.config;

import com.ecritic.ecritic_authentication_service.entrypoint.filter.ApplicationRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

    private final ApplicationRequestFilter applicationRequestFilter;

    @Bean
    public FilterRegistrationBean<ApplicationRequestFilter> authenticationFilter() {

        FilterRegistrationBean<ApplicationRequestFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(applicationRequestFilter);
        registrationBean.addUrlPatterns("*");

        return registrationBean;
    }
}
