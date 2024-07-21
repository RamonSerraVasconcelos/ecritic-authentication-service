package com.ecritic.ecritic_authentication_service.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class IdToken extends Token {

    private String email;
    private String name;
    private String picture;
}
