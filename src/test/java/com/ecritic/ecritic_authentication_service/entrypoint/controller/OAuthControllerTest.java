package com.ecritic.ecritic_authentication_service.entrypoint.controller;

import com.ecritic.ecritic_authentication_service.core.fixture.AuthenticationDataFixture;
import com.ecritic.ecritic_authentication_service.core.model.AuthenticationData;
import com.ecritic.ecritic_authentication_service.core.usecase.oauth2.GenerateRedirectInfoUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.oauth2.ValidateCallbackUseCase;
import com.ecritic.ecritic_authentication_service.dataprovider.api.fixture.AuthenticationResponseDataFixture;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthenticationResponseData;
import com.ecritic.ecritic_authentication_service.entrypoint.mapper.AuthenticationResponseDataMapper;
import com.ecritic.ecritic_authentication_service.exception.handler.ApplicationExceptionHandler;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.net.URI;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OAuthController.class)
@ContextConfiguration(classes = {OAuthController.class, ApplicationExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class OAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GenerateRedirectInfoUseCase generateRedirectInfoUseCase;

    @MockBean
    private ValidateCallbackUseCase validateCallbackUseCase;

    @MockBean
    private AuthenticationResponseDataMapper authenticationResponseDataMapper;

    @Test
    void givenRequestToLoginEndpoint_ThenGenerate_andReturnAuthorizationUriResponse() throws Exception {
        URI uri = URI.create("http://google.com/redirect");

        when(generateRedirectInfoUseCase.execute(any(), any())).thenReturn(uri);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post("/oauth2/login")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("authServerName", "google")
                .param("redirectUri", "http://localhost:8080/redirect");

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorizationUri").value(uri.toString()));
    }

    @ParameterizedTest
    @MethodSource("provideLoginEndpointRequestData")
    void givenRequestToLoginEndpoint_whenRequiredParametersAreMissing_thenReturnBadRequest(MultiValueMap<String, String> params, String errorDetail) throws Exception {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .post("/oauth2/login")
                .params(params)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorResponseCode.ECRITICAUTH_01.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorResponseCode.ECRITICAUTH_01.getMessage()))
                .andExpect(jsonPath("$.detail").value(errorDetail))
                .andReturn();
    }

    @Test
    void givenRequestToCallbackEndpoint_thenValidate_andReturnAuthenticationData() throws Exception {
        AuthenticationData authenticationData = AuthenticationDataFixture.load();
        AuthenticationResponseData authenticationResponseData = AuthenticationResponseDataFixture.load();

        String code = "code";
        String state = "state";

        when(validateCallbackUseCase.execute(code, state, null, null)).thenReturn(authenticationData);
        when(authenticationResponseDataMapper.authorizationDataToAuthorizationResponseData(any())).thenReturn(authenticationResponseData);

        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = MockMvcRequestBuilders
                .get("/oauth2/callback")
                .param("code", code)
                .param("state", state);

        mockMvc.perform(mockHttpServletRequestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(authenticationResponseData.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(authenticationResponseData.getRefreshToken()))
                .andExpect(jsonPath("$.tokenType").value(authenticationResponseData.getTokenType()))
                .andExpect(jsonPath("$.expiresIn").value(authenticationResponseData.getExpiresIn()))
                .andExpect(jsonPath("$.refreshExpiresIn").value(authenticationResponseData.getRefreshExpiresIn()));
    }

    static Stream<Arguments> provideLoginEndpointRequestData() {
        MultiValueMap<String, String> queryParams1 = new LinkedMultiValueMap<>();
        ;
        queryParams1.add("authServerName", "google");

        MultiValueMap<String, String> queryParams2 = new LinkedMultiValueMap<>();
        queryParams2.add("redirectUri", "http://localhost:8080/redirect");

        return Stream.of(
                Arguments.of(queryParams1, "Required request parameter 'redirectUri' for method parameter type String is not present"),
                Arguments.of(queryParams2, "Required request parameter 'authServerName' for method parameter type String is not present")
        );
    }
}