package com.ecritic.ecritic_authentication_service.entrypoint.controller;

import com.ecritic.ecritic_authentication_service.core.fixture.AccessTokenFixture;
import com.ecritic.ecritic_authentication_service.core.fixture.AuthenticationDataFixture;
import com.ecritic.ecritic_authentication_service.core.model.AccessToken;
import com.ecritic.ecritic_authentication_service.core.model.AuthenticationData;
import com.ecritic.ecritic_authentication_service.core.usecase.DecryptAccessTokenUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.RefreshUserTokenUseCase;
import com.ecritic.ecritic_authentication_service.core.usecase.SignInUserUseCase;
import com.ecritic.ecritic_authentication_service.dataprovider.api.fixture.AuthenticationResponseDataFixture;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.AuthenticationResponseData;
import com.ecritic.ecritic_authentication_service.entrypoint.dto.RefreshTokenRequest;
import com.ecritic.ecritic_authentication_service.entrypoint.mapper.AuthenticationResponseDataMapper;
import com.ecritic.ecritic_authentication_service.exception.handler.ApplicationExceptionHandler;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SignInUserUseCase signInUserUseCase;

    @MockBean
    private DecryptAccessTokenUseCase decryptAccessTokenUseCase;

    @MockBean
    private RefreshUserTokenUseCase refreshUserTokenUseCase;

    @MockBean
    private AuthenticationResponseDataMapper authenticationResponseDataMapper;

    @Test
    void givenRequestToLoginEndpoint_thenLogAndReturnAuthorizationInfoData() throws Exception {
        AuthenticationData authenticationData = AuthenticationDataFixture.load();
        AuthenticationResponseData authenticationResponseData = AuthenticationResponseDataFixture.load();

        String email = "email@email.com";
        String password = "password";

        when(signInUserUseCase.execute(any(), any())).thenReturn(authenticationData);
        when(authenticationResponseDataMapper.authorizationDataToAuthorizationResponseData(any())).thenReturn(authenticationResponseData);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/login")
                .formField("email", email)
                .formField("password", password)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(authenticationResponseData.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(authenticationResponseData.getRefreshToken()))
                .andExpect(jsonPath("$.tokenType").value(authenticationResponseData.getTokenType()))
                .andExpect(jsonPath("$.expiresIn").value(authenticationResponseData.getExpiresIn()))
                .andExpect(jsonPath("$.refreshExpiresIn").value(authenticationResponseData.getRefreshExpiresIn()));

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

    @Test
    void givenRequestToTokenEndpoint_thenGenerate_andReturnAuthenticationData() throws Exception {
        AuthenticationData authenticationData = AuthenticationDataFixture.load();
        AuthenticationResponseData authenticationResponseData = AuthenticationResponseDataFixture.load();
        RefreshTokenRequest refreshTokenRequest = RefreshTokenRequest.builder()
                .refreshToken("eysdu9oadiudniajsndiasdnasiudasiudasiudhasidhas")
                .build();

        when(refreshUserTokenUseCase.execute(any())).thenReturn(authenticationData);
        when(authenticationResponseDataMapper.authorizationDataToAuthorizationResponseData(any())).thenReturn(authenticationResponseData);

        String body = objectMapper.writeValueAsString(refreshTokenRequest);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/token")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(authenticationResponseData.getAccessToken()))
                .andExpect(jsonPath("$.refreshToken").value(authenticationResponseData.getRefreshToken()))
                .andExpect(jsonPath("$.tokenType").value(authenticationResponseData.getTokenType()))
                .andExpect(jsonPath("$.expiresIn").value(authenticationResponseData.getExpiresIn()))
                .andExpect(jsonPath("$.refreshExpiresIn").value(authenticationResponseData.getRefreshExpiresIn()));
    }

    @Test
    void givenRequestToTokenEndpoint_whenRequestBodyIsMissing_thenReturnBadRequest() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/token")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorResponseCode.ECRITICAUTH_01.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorResponseCode.ECRITICAUTH_01.getMessage()))
                .andExpect(jsonPath("$.detail").value("Required request body is missing"))
                .andReturn();
    }

    @Test
    void givenRequestToValidateTokenEndpoint_thenValidate_andReturnUnsignedToken() throws Exception {
        AccessToken accessToken = AccessTokenFixture.load();
        accessToken.setJwt("eyasidjasoidjasoidjasoidasoidhniuwdqiqwd");

        when(decryptAccessTokenUseCase.execute(any())).thenReturn(accessToken);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/token/introspect")
                .header("Authorization", "eysdu9oadiudniajsndiasdnasiudasiudasiudhasidhasasjdasiodjaoiwdjaosdjasoidjasoi")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(accessToken.getJwt()))
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").doesNotExist())
                .andExpect(jsonPath("$.refreshExpiresIn").doesNotExist());
    }

    @Test
    void givenRequestToValidateToken_whenHeaderIsMissing_thenReturnBadRequest() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/auth/token/introspect")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(ErrorResponseCode.ECRITICAUTH_02.getCode()))
                .andExpect(jsonPath("$.message").value(ErrorResponseCode.ECRITICAUTH_02.getMessage()))
                .andExpect(jsonPath("$.detail").value("Required request header 'Authorization' for method parameter type String is not present"))
                .andReturn();
    }
}