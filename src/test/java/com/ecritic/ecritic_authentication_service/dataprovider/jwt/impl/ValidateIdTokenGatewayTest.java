package com.ecritic.ecritic_authentication_service.dataprovider.jwt.impl;

import com.ecritic.ecritic_authentication_service.core.model.IdToken;
import com.ecritic.ecritic_authentication_service.exception.BusinessViolationException;
import com.ecritic.ecritic_authentication_service.exception.handler.ErrorResponseCode;
import com.nimbusds.jose.jwk.JWKSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ValidateIdTokenGatewayTest {

    @InjectMocks
    private ValidateIdTokenGateway validateIdTokenGateway;

    private static final String ID_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IjQ1MjljNDA5Zjc3YTEwNmZiNjdlZTFhODVkMTY4ZmQyY2ZiN2MwYjciLCJ0eXAiOiJKV1QifQ.eyJpc3MiOiJodHRwczovL2FjY291bnRzLmdvb2dsZS5jb20iLCJhenAiOiIxNDEwMDc4MzQ4MjQtNzM5cTlvbnI4YzhhbmNlNnBmYzhmaXNzNjZlZmE3ZGQuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJhdWQiOiIxNDEwMDc4MzQ4MjQtNzM5cTlvbnI4YzhhbmNlNnBmYzhmaXNzNjZlZmE3ZGQuYXBwcy5nb29nbGV1c2VyY29udGVudC5jb20iLCJzdWIiOiIxMTM2ODY2NzUyMjk2Nzk4NTkyNTIiLCJlbWFpbCI6InBvaXRyYTU2QGdtYWlsLmNvbSIsImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJhdF9oYXNoIjoibzFURmsxWkNFN3JmaV9yNHk5SkhOQSIsIm5hbWUiOiJSYW1vbiIsInBpY3R1cmUiOiJodHRwczovL2xoMy5nb29nbGV1c2VyY29udGVudC5jb20vYS9BQ2c4b2NMUVQtR2xzM0gzVW9ZQzdGTlhySUY2OVVXQ08xRy0wMEFZdHQ0bTVnRmEyaFpYc0xrPXM5Ni1jIiwiZ2l2ZW5fbmFtZSI6IlJhbW9uIiwiaWF0IjoxNzIzMTQyNzQxLCJleHAiOjE3MjMxNDYzNDF9.LYOL5EmACIzKXTpnglXjo0Bcwrx7sDeAF50yUMsayhmkcec79dvsQsNeiaPx5RAoIilXlydq6sMwD3ckb9-LHgAj8DVIFORNWMb-301b_p0P6WuMCI5SA_lIjkqIyexXWKefcpta2qIlx27JDuMZcv6NmQfIkv0BSDV8OZ9fSp_3s5VvLeqmw2sZ7ThaQClS_4Sgq4mOr1DJ9wIkIiALCnoaL7nesi7zqvYE0lP0f5IZyZdrZOWkogFA1IduM5a6aFGKq0INHL33TRO-elvvPTqRyHMUmoVotnv6NuDil8pbwND-KbKkbj7l6aFCdQDqC0Vnf9zSESP6uV0EznE4Tg";

    private static final String JWKS = """
            {
              "keys": [
                {
                  "kid": "e26d917b1fe8de13382aa7cc9a1d6e93262f33e2",
                  "use": "sig",
                  "alg": "RS256",
                  "kty": "RSA",
                  "n": "2hJ7F-aJlN2hTrOelbdFB2WDlzS5oscgd5UBL_5NruogKCGFQFMk_K3d5L6N9P6mNxKr60IeGPg8zzl41iE9qQmvG9yLMA-VCW2f6gTvUkJBluYJ4wByN8Hr98tJFIvzE1q4iWclwyqiiWXyTiXfhyL0n-aMa6OgMaMLWsOFRKPEFR9ajeVqqc8GFjz4Kkij1dHWmkd_AU0wjJqDOl7wdCcLLy9bmlUwaJ4p29nRVK_KrNEL1E5PpK5Bwo6_TrXCLrAx_p3xJ5IZctwzoFkl3xpqbJOZax6s8CrHKXmG03TkEQt5a9H3bupQPaNU-bYq9E1_OvycBY6bWwD23UdwUw",
                  "e": "AQAB"
                },
                {
                  "n": "1crrYmsX8OVzrN9BTDD4RlVJDqSQIEbRby9ELqTmCpW1Qtt7y-pdmLPqlYG1ND5mprkTA83S7g_dcsxuV4wxK4_Vv5a8IBn86HfAX4VfCCOzqBYgACN6hlaffzPIWL1QA8yZ4w-D0fnN3xC5ULhtmtBG23qi__4yEo_FIY6irvbHrpRNI_-vjxFokm2X3ENP2ZOwgNhDIthwJo8l1KNbZa1riAJVcF86zWILQTy756hh8eH1Kt05wsGB3DeGPNV55zYv6sB2bzxARsVYAtCRJ8c28FYWwU8dCRJ70eJEmY4aKFOBO5g4fwYJlvMm9Le7qgAUH5-7wO52BayqXmqAOQ",
                  "kty": "RSA",
                  "e": "AQAB",
                  "kid": "4529c409f77a106fb67ee1a85d168fd2cfb7c0b7",
                  "alg": "RS256",
                  "use": "sig"
                }
              ]
            }""";

    private static final String SECOND_JWKS = """
            {
                "keys": [
                    {
                        "kty": "RSA",
                        "use": "sig",
                        "kid": "23ntaxfH9Gk-8D_USzoAJgwjyt8",
                        "x5t": "23ntaxfH9Gk-8D_USzoAJgwjyt8",
                        "n": "hu2SJrLlDOUtU2s9T6-6OVGEPaba2zIT2_Jl50f4NGG-r-GyQdaOzTFASfAfMkMfMQMRnabqd-dp_Ooqha473bw6DMbM23nv2uhBn5Afp-S1W_d4NxEhfNlN1Tgjx3Sh6UblBSFCE4JGkugSkLi2SVouy43seskesQotXGVNv4iboFm4yO_twlMCG9EDwza32y6WZtV8i9gkQP42OfK0X1qy6EUz2DN7cpfZtmkNtsFJhFf9waOvNCR95LVCPGafeCOMAQEvu1VO3mrBSIg7Izu0CzvuaBQTwnGv29Ggxc3GO4gvb_OStkkmfIwchu3A8F6e0aJ4Ys8PFP7z7Z8lqQ",
                        "e": "AQAB",
                        "x5c": [
                            "MIIC/jCCAeagAwIBAgIJAJB4tJM2GkZjMA0GCSqGSIb3DQEBCwUAMC0xKzApBgNVBAMTImFjY291bnRzLmFjY2Vzc2NvbnRyb2wud2luZG93cy5uZXQwHhcNMjQwNTIyMTg1ODQwWhcNMjkwNTIyMTg1ODQwWjAtMSswKQYDVQQDEyJhY2NvdW50cy5hY2Nlc3Njb250cm9sLndpbmRvd3MubmV0MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhu2SJrLlDOUtU2s9T6+6OVGEPaba2zIT2/Jl50f4NGG+r+GyQdaOzTFASfAfMkMfMQMRnabqd+dp/Ooqha473bw6DMbM23nv2uhBn5Afp+S1W/d4NxEhfNlN1Tgjx3Sh6UblBSFCE4JGkugSkLi2SVouy43seskesQotXGVNv4iboFm4yO/twlMCG9EDwza32y6WZtV8i9gkQP42OfK0X1qy6EUz2DN7cpfZtmkNtsFJhFf9waOvNCR95LVCPGafeCOMAQEvu1VO3mrBSIg7Izu0CzvuaBQTwnGv29Ggxc3GO4gvb/OStkkmfIwchu3A8F6e0aJ4Ys8PFP7z7Z8lqQIDAQABoyEwHzAdBgNVHQ4EFgQUeGPdsxkVp8lIRku0u41SCzqW7LIwDQYJKoZIhvcNAQELBQADggEBAHMJCPO473QQJtTXJ49OhZ48kVCiVgbut+xElHxvBWQrfJ4Zb6WAi2RudjwrpwchVBciwjIelp/3Ryp5rVL94D479Ta/C5BzWNm9LsZCw3rPrsIvUdx26GmfQomHyL18AJQyBj8jZ+pVvdprvbV7v586TcgY24pW018IiYGQEO/fR8DSO4eN8ekTvT8hODBoKiJ9NFy+BruqW1AbMDptH12uzpU/N9bftysnWeDJEVZd5Rj8u8F9MRbB6V7dzxdoswaKkiJbxt+JrZgdtHSFqz6rDypIkumYwUkyiwH4/GQGPiyBLFbRp1EYVa3SFwAEmhl4a7On05aHVnOfCoyj/qA="
                        ],
                        "issuer": "https://login.microsoftonline.com/{tenantid}/v2.0"
                    }
                ]
            }""";

    @Test
    void givenValidIdToken_whenSignatureIsValid_thenReturnIdToken() throws ParseException {
        IdToken idToken = validateIdTokenGateway.execute(ID_TOKEN, JWKSet.parse(JWKS));

        assertThat(idToken).isNotNull();
        assertThat(idToken.getIssuer()).isNotNull();
        assertThat(idToken.getAud()).isNotNull();
        assertThat(idToken.getName()).isNotNull();
        assertThat(idToken.getEmail()).isNotNull();
        assertThat(idToken.getIssuedAt()).isNotNull();
        assertThat(idToken.getExpiresAt()).isNotNull();
    }

    @Test
    void givenInvalidIdToken_thenThrowBusinessViolationException() {
        String invalidIdToken = "eynaosidnaosdnasodnasdasd.asdoiasmdoiasd.asdoiasiodasd";

        BusinessViolationException ex = assertThrows(BusinessViolationException.class, () -> validateIdTokenGateway.execute(invalidIdToken, JWKSet.parse(JWKS)));

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_12.getCode());
    }

    @Test
    void givenInvalidSignature_thenThrowBusinessViolationException() {
        BusinessViolationException ex = assertThrows(BusinessViolationException.class,
                () -> validateIdTokenGateway.execute(ID_TOKEN, JWKSet.parse(SECOND_JWKS)));

        assertThat(ex.getErrorResponse().getCode()).isEqualTo(ErrorResponseCode.ECRITICAUTH_12.getCode());
    }
}