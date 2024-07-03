package com.ecritic.ecritic_authentication_service.dataprovider.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "refreshToken")
public class RefreshTokenEntity {

    @Id
    private String id;
    private String issuer;
    private Set<String> aud;
    private String userId;
    private RoleEntity userRole;
    private boolean active;
    private LocalDateTime issuedAt;

    @Indexed(expireAfterSeconds = 0)
    private LocalDateTime expiresAt;
}
