package com.ecritic.ecritic_authentication_service.core.fixture;

import com.ecritic.ecritic_authentication_service.core.model.IdToken;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class IdTokenFixture {

    public static IdToken load() {
        IdToken idToken = new IdToken();

        idToken.setId(UUID.randomUUID());
        idToken.setIssuer("google");
        idToken.setAud(Set.of("esdj782hdasdasd"));
        idToken.setJwt("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        idToken.setEmail("johndoe@gmail.com");
        idToken.setName("John Doe");
        idToken.setPicture("https://example.com/johndoe.jpg");
        idToken.setIssuedAt(LocalDateTime.now().minusSeconds(30));
        idToken.setExpiresAt(LocalDateTime.now().plusMinutes(10));

        return idToken;
    }
}
