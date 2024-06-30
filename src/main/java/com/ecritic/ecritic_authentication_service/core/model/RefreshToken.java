package com.ecritic.ecritic_authentication_service.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RefreshToken extends Token {

    private boolean active;
}
