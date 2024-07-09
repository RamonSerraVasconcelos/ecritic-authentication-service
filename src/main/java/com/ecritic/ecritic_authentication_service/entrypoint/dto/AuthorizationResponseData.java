package com.ecritic.ecritic_authentication_service.entrypoint.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizationResponseData {

    private String accessToken;

    private String refreshToken;

    private String tokenType;

    private Integer expiresIn;

    private Integer refreshExpiresIn;
}
