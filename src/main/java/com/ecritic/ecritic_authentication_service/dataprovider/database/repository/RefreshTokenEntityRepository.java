package com.ecritic.ecritic_authentication_service.dataprovider.database.repository;

import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.RefreshTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RefreshTokenEntityRepository extends MongoRepository<RefreshTokenEntity, String>, RefreshTokenEntityCustomRepository {
}
