package com.ecritic.ecritic_authentication_service.dataprovider.database.repository.impl;

import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.RefreshTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.RefreshTokenEntityCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class RefreshTokenEntityRepositoryImpl implements RefreshTokenEntityCustomRepository {

    private final MongoTemplate mongoTemplate;

    public void invalidateByUserId(UUID userId) {
        Query query = new Query(Criteria.where("userId").is(userId.toString()));

        Update update = new Update();
        update.set("active", false);

        mongoTemplate.updateMulti(query, update, RefreshTokenEntity.class);
    }
}
