package com.ecritic.ecritic_authentication_service.dataprovider.database.mapper;

import com.ecritic.ecritic_authentication_service.core.model.RefreshToken;
import com.ecritic.ecritic_authentication_service.core.model.User;
import com.ecritic.ecritic_authentication_service.core.model.enums.Role;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.RefreshTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RefreshTokenEntityMapper {

    public RefreshTokenEntity refreshTokenToRefreshTokenEntity(RefreshToken refreshToken) {
        return RefreshTokenEntity.builder()
                .id(refreshToken.getId().toString())
                .userId(refreshToken.getUser().getId().toString())
                .issuer(refreshToken.getIssuer())
                .aud(refreshToken.getAud())
                .userRole(RoleEntity.valueOf(refreshToken.getUser().getRole().name()))
                .active(refreshToken.isActive())
                .issuedAt(refreshToken.getIssuedAt())
                .expiresAt(refreshToken.getExpiresAt())
                .build();
    }

    public RefreshToken refreshTokenEntityToRefreshToken(RefreshTokenEntity refreshTokenEntity) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setId(UUID.fromString(refreshTokenEntity.getId()));
        refreshToken.setIssuer(refreshTokenEntity.getIssuer());
        refreshToken.setAud(refreshTokenEntity.getAud());
        refreshToken.setUser(User.builder()
                .id(UUID.fromString(refreshTokenEntity.getUserId()))
                .role(Role.valueOf(refreshTokenEntity.getUserRole().name()))
                .build());
        refreshToken.setActive(refreshTokenEntity.isActive());
        refreshToken.setIssuedAt(refreshTokenEntity.getIssuedAt());
        refreshToken.setExpiresAt(refreshTokenEntity.getExpiresAt());

        return refreshToken;
    }
}
