package com.ecritic.ecritic_authentication_service.dataprovider.database.repository;

import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.ExternalTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExternalTokenEntityRepository extends MongoRepository<ExternalTokenEntity, String>, ExternalTokenEntityCustomRepository {
}
