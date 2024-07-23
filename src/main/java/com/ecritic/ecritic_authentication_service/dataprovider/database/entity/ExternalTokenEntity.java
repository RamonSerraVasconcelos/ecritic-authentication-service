package com.ecritic.ecritic_authentication_service.dataprovider.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(collection = "externalToken")
public class ExternalTokenEntity {

    private String clientId;
    private String userId;
    private String refreshToken;
    private String tokenType;
    private String scope;
}
