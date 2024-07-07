package com.ecritic.ecritic_authentication_service.entrypoint.controller;

import com.ecritic.ecritic_authentication_service.core.fixture.AuthorizationDataFixture;
import com.ecritic.ecritic_authentication_service.core.model.AuthorizationData;
import com.ecritic.ecritic_authentication_service.core.usecase.DecryptAccessTokenUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.RefreshUserTokenUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.SignInUserUseCase;
import com.ecritic.ecritic_authentication_service.dataprovider.api.fixture.AuthorizationResponseDataFixture;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthorizationResponseData;
import com.ecritic.ecritic_authentication_service.entrypoint.mapper.AuthorizationResponseDataMapper;
import com.ecritic.ecritic_authentication_service.exception.handler.ApplicationExceptionHandler;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@ContextConfiguration(classes = {AuthController.class, ApplicationExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SignInUserUseCase signInUserUseCase;

    @MockBean
    private DecryptAccessTokenUseCase decryptAccessTokenUseCase;

    @MockBean
    private RefreshUserTokenUseCase refreshUserTokenUseCase;

    @MockBean
    private AuthorizationResponseDataMapper authorizationResponseDataMapper;

    @Test
    void givenRequestToLoginEndpoint_thenLogAndReturnAuthorizationInfoData() throws Exception {
        AuthorizationData authorizationData = AuthorizationDataFixture.load();
        AuthorizationResponseData authorizationResponseData = AuthorizationResponseDataFixture.load();

        String email = "email@email.com";
        String password = "password";

        when(signInUserUseCase.execute(any(), any())).thenReturn(authorizationData);
        when(authorizationResponseDataMapper.authorizationDataToAuthorizationResponseData(any())).thenReturn(authorizationResponseData);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/login")
                .formField("email", email)
                .formField("password", password)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(authorizationResponseData.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(authorizationResponseData.getRefreshToken()))
                .andExpect(jsonPath("$.tokenType").value(authorizationResponseData.getTokenType()))
                .andExpect(jsonPath("$.expiresIn").value(authorizationResponseData.getExpiresIn()))
                .andExpect(jsonPath("$.refreshExpiresIn").value(authorizationResponseData.getRefreshExpiresIn()));

    }

    @Test
    void givenRequestToLoginEndpoint_whenEmailIsMissing_thenReturnBadRequest() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/login")
                .formField("password", "12345678")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorResponseCode.ECRITICAUTH_01.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorResponseCode.ECRITICAUTH_01.getMessage()))
                .andExpect(jsonPath("$.detail").value("Required request parameter 'email' for method parameter type String is not present"))
                .andReturn();
    }

    @Test
    void givenRequestToLoginEndpoint_whenPasswordIsMissing_thenReturnBadRequest() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/login")
                .formField("email", "email@email.com")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorResponseCode.ECRITICAUTH_01.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorResponseCode.ECRITICAUTH_01.getMessage()))
                .andExpect(jsonPath("$.detail").value("Required request parameter 'password' for method parameter type String is not present"))
                .andReturn();
    }
}