package com.ecritic.ecritic_authentication_service.dataprovider.messaging.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserStatusUpdateMessage {

    private UUID userId;
    private boolean active;
}
