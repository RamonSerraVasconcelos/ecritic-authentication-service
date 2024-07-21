package com.ecritic.ecritic_authentication_service.dataprovider.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "authorizationServer")
public class AuthorizationServerEntity {

    @Id
    private String clientId;
    private String clientSecret;
    private String name;
    private String responseType;
    private URI authEndpoint;
    private URI tokenEndpoint;
    private URI jwksEndpoint;
    private Set<URI> redirectUris;
    private List<String> scopes;
    private Map<String, String> additionalParams;
}
