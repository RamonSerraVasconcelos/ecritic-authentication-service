package com.ecritic.ecritic_authentication_service.dataprovider.messaging.consumer;

import com.ecritic.ecritic_authentication_service.config.properties.KafkaProperties;
import com.ecritic.ecritic_authentication_service.core.model.UserStatusUpdateUseCase;
import com.ecritic.ecritic_authentication_service.dataprovider.messaging.entity.UserStatusUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserStatusUpdateConsumer {

    private final KafkaProperties kafkaProperties;

    private final UserStatusUpdateUseCase userStatusUpdateUseCase;

    @KafkaListener(topics = "${spring.kafka.user-status-update-topic}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "userStatusUpdateListenerContainerFactory")
    public void execute(UserStatusUpdateMessage userStatusUpdateMessage) {
        log.info("User status update message received: [{}] on topic: [{}]", userStatusUpdateMessage, kafkaProperties.getUserStatusUpdateTopic());

        try {
            userStatusUpdateUseCase.execute(userStatusUpdateMessage.getUserId(), userStatusUpdateMessage.isActive());
        } catch (Exception ex) {
            log.error("Error processing user status update message", ex);
        }
    }
}
