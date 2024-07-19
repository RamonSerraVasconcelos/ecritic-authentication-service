package com.ecritic.ecritic_authentication_service.dataprovider.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ClientErrorResponse {

    private String code;
    private String message;
    private String detail;
}
