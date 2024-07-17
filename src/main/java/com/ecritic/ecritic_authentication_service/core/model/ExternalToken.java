package com.ecritic.ecritic_authentication_service.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExternalToken {

    private String accessToken;
    private String refreshToken;
    private String tokenType;
    private String scope;
    private Long expiresIn;
}
