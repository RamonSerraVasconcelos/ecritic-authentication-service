package com.ecritic.ecritic_authentication_service.dataprovider.api.entity;

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
public class UserEntity {

    private String id;
    private String email;
    private String password;
    private String role;
    private boolean active;
}
