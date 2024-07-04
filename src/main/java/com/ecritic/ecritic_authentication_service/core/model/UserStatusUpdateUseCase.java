package com.ecritic.ecritic_authentication_service.core.model;

import com.ecritic.ecritic_authentication_service.config.properties.ApplicationProperties;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.BlacklistUserBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.InvalidateUserTokensBoundary;
import com.ecritic.ecritic_authentication_service.core.usecase.boundary.UnblacklistUserBoundary;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserStatusUpdateUseCase {

    private final InvalidateUserTokensBoundary invalidateUserTokensBoundary;

    private final BlacklistUserBoundary blacklistUserBoundary;

    private final UnblacklistUserBoundary unblacklistUserBoundary;

    private final ApplicationProperties applicationProperties;

    public void execute(UUID userId, boolean active) {
        try {
            if (!active) {
                log.info("Invalidating tokens and blacklisting userId: [{}]", userId);
                
                invalidateUserTokensBoundary.execute(userId);
                blacklistUserBoundary.execute(userId, applicationProperties.getJwtExpiration());
            } else {
                log.info("Unblacklisting userId: [{}]", userId);

                unblacklistUserBoundary.execute(userId);
            }
        } catch (Exception ex) {
            log.error("Error updating authentication tokens for userId: [{}]", userId, ex);
        }
    }
}
