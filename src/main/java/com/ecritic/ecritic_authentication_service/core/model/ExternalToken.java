package com.ecritic.ecritic_authentication_service.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExternalToken {

    private UUID id;
    private String clientId;
    private UUID userId;
    private String accessToken;
    private String refreshToken;
    private String idToken;
    private String tokenType;
    private String scope;
    private Long expiresIn;
}
