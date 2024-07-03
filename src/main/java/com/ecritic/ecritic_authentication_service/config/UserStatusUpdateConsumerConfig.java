package com.ecritic.ecritic_authentication_service.config;

import com.ecritic.ecritic_authentication_service.dataprovider.messaging.entity.UserStatusUpdateMessage;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class UserStatusUpdateConsumerConfig {

    @Autowired
    private KafkaConsumersConfig consumersConfig;

    @Bean
    public ConsumerFactory<String, UserStatusUpdateMessage> userStatusUpdateKafkaConsumerFactory() {
        DefaultJackson2JavaTypeMapper jsonMapper = new DefaultJackson2JavaTypeMapper();
        jsonMapper.addTrustedPackages("*");

        JsonDeserializer<UserStatusUpdateMessage> jsonDeserializer = new JsonDeserializer<>(UserStatusUpdateMessage.class);
        jsonDeserializer.setTypeMapper(jsonMapper);

        return new DefaultKafkaConsumerFactory<>(consumersConfig.consumerConfigs(jsonDeserializer), new StringDeserializer(), jsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserStatusUpdateMessage> userStatusUpdateListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserStatusUpdateMessage> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userStatusUpdateKafkaConsumerFactory());
        return factory;
    }
}
