package com.ecritic.ecritic_authentication_service.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Token {

    private UUID id;
    private String jwt;
    private User user;
    private String issuer;
    private Set<String> aud;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
}
