package com.ecritic.ecritic_authentication_service.dataprovider.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "externalToken")
public class ExternalTokenEntity {

    @Id
    private String id;
    private String clientId;
    private String userId;
    private String refreshToken;
    private String tokenType;
    private String scope;
}
