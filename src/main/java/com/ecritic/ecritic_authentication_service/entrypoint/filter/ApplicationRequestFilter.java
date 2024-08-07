package com.ecritic.ecritic_authentication_service.entrypoint.filter;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationRequestHeaders;
import com.ecritic.ecritic_authentication_service.config.properties.ThreadRequestProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

import static java.util.Objects.isNull;

@Component
@Slf4j
public class ApplicationRequestFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        RequestHeaders headers = getHeaders(request);

        response.setHeader(ApplicationRequestHeaders.REQUEST_ID.getValue(), headers.getRequestId());

        ThreadRequestProperties.setRequestId(headers.getRequestId());

        logRequest(request, headers);
        filterChain.doFilter(request, response);
    }

    private RequestHeaders getHeaders(HttpServletRequest request) {
        String requestId = request.getHeader("X-Request-Id");

        if (isNull(requestId)) {
            requestId = UUID.randomUUID().toString();
        }

        return RequestHeaders.builder()
                .requestId(requestId)
                .authorization(request.getHeader(ApplicationRequestHeaders.AUTHORIZATION.getValue()))
                .build();
    }

    private void logRequest(HttpServletRequest request, RequestHeaders headers) {
        log.info("Request INFO - Method: [{}], URI: [{}], Headers: [{}]", request.getMethod(), request.getRequestURI(), headers.toString());
    }
}
