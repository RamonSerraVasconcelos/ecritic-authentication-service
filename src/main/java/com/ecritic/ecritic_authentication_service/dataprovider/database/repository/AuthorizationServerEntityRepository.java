package com.ecritic.ecritic_authentication_service.dataprovider.database.repository;

import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.AuthorizationServerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthorizationServerEntityRepository extends MongoRepository<AuthorizationServerEntity, String> {

    AuthorizationServerEntity findByName(String name);
}
