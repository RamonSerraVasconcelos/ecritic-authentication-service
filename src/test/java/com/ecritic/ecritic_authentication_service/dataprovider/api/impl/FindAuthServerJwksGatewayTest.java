package com.ecritic.ecritic_authentication_service.dataprovider.api.impl;

import com.ecritic.ecritic_authentication_service.dataprovider.api.client.AuthorizationClient;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.nimbusds.jose.jwk.JWKSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FindAuthServerJwksGatewayTest {

    @InjectMocks
    private FindAuthServerJwksGateway findAuthServerJwksGateway;

    @Mock
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Mock
    private ObjectProvider<HttpMessageConverterCustomizer> customizers;

    @Mock
    private AuthorizationClient authorizationClient;

    private static final String JWKS = """
            {
              "keys": [
                {
                  "use": "sig",
                  "n": "2hJ7F-aJlN2hTrOelbdFB2WDlzS5oscgd5UBL_5NruogKCGFQFMk_K3d5L6N9P6mNxKr60IeGPg8zzl41iE9qQmvG9yLMA-VCW2f6gTvUkJBluYJ4wByN8Hr98tJFIvzE1q4iWclwyqiiWXyTiXfhyL0n-aMa6OgMaMLWsOFRKPEFR9ajeVqqc8GFjz4Kkij1dHWmkd_AU0wjJqDOl7wdCcLLy9bmlUwaJ4p29nRVK_KrNEL1E5PpK5Bwo6_TrXCLrAx_p3xJ5IZctwzoFkl3xpqbJOZax6s8CrHKXmG03TkEQt5a9H3bupQPaNU-bYq9E1_OvycBY6bWwD23UdwUw",
                  "e": "AQAB",
                  "kty": "RSA",
                  "kid": "e26d917b1fe8de13382aa7cc9a1d6e93262f33e2",
                  "alg": "RS256"
                },
                {
                  "use": "sig",
                  "n": "1crrYmsX8OVzrN9BTDD4RlVJDqSQIEbRby9ELqTmCpW1Qtt7y-pdmLPqlYG1ND5mprkTA83S7g_dcsxuV4wxK4_Vv5a8IBn86HfAX4VfCCOzqBYgACN6hlaffzPIWL1QA8yZ4w-D0fnN3xC5ULhtmtBG23qi__4yEo_FIY6irvbHrpRNI_-vjxFokm2X3ENP2ZOwgNhDIthwJo8l1KNbZa1riAJVcF86zWILQTy756hh8eH1Kt05wsGB3DeGPNV55zYv6sB2bzxARsVYAtCRJ8c28FYWwU8dCRJ70eJEmY4aKFOBO5g4fwYJlvMm9Le7qgAUH5-7wO52BayqXmqAOQ",
                  "kty": "RSA",
                  "e": "AQAB",
                  "alg": "RS256",
                  "kid": "4529c409f77a106fb67ee1a85d168fd2cfb7c0b7"
                }
              ]
            }""";

    @BeforeEach
    public void setup() {
        findAuthServerJwksGateway = spy(new FindAuthServerJwksGateway(messageConverters, customizers));
    }

    @Test
    public void givenExecution_thenFind_andReturnJwks() throws Exception {
        URI jwksUri = new URI("https://example.com/.well-known/jwks.json");

        doReturn(authorizationClient).when(findAuthServerJwksGateway).getFeignClient();
        when(authorizationClient.getJwks(jwksUri)).thenReturn(JWKS);

        JWKSet jwkSet = findAuthServerJwksGateway.execute(jwksUri);

        assertThat(jwkSet).isNotNull();
        assertThat(jwkSet.getKeys()).isNotEmpty().hasSize(2);
    }

    @Test
    public void givenInvalidJwks_thenThrowBusinessViolationException() throws Exception {
        URI jwksUri = new URI("https://example.com/.well-known/jwks.json");
        String invalidJwksString = "invalid jwks";

        doReturn(authorizationClient).when(findAuthServerJwksGateway).getFeignClient();
        when(authorizationClient.getJwks(jwksUri)).thenReturn(invalidJwksString);

        BusinessViolationException ex = assertThrows(BusinessViolationException.class, () -> {
            findAuthServerJwksGateway.execute(jwksUri);
        });

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_12.getCode());
    }
}