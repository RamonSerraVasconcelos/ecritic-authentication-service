package com.ecritic.ecritic_authentication_service.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthorizationServer {

    private String clientId;
    private String clientSecret;
    private String name;
    private String responseType;
    private URI authEndpoint;
    private URI tokenEndpoint;
    private Set<URI> redirectUris;
    private List<String> scopes;

    public String getFormattedScopes() {
        return URLEncoder.encode(String.join(" ", scopes), StandardCharsets.UTF_8);
    }
}
