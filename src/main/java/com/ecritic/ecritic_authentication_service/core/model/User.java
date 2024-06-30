package com.ecritic.ecritic_authentication_service.core.model;

import com.ecritic.ecritic_authentication_service.core.model.enums.Role;
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
public class User {

    private UUID id;
    private String email;
    private String password;
    private Role role;
    private boolean active;
}
