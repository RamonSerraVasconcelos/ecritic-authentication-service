package com.ecritic.ecritic_authentication_service.dataprovider.database.repository.impl;

import com.ecritic.ecritic_authentication_service.dataprovider.database.entity.ExternalTokenEntity;
import com.ecritic.ecritic_authentication_service.dataprovider.database.repository.ExternalTokenEntityCustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ExternalTokenEntityRepositoryImpl implements ExternalTokenEntityCustomRepository {

    private final MongoTemplate mongoTemplate;

    public void deleteUserTokens(UUID userId, String clientId, UUID currentTokenId) {
        Query query = new Query(Criteria.where("userId").is(userId.toString()));
        query.addCriteria(Criteria.where("clientId").is(clientId));
        query.addCriteria(Criteria.where("id").ne(currentTokenId.toString()));

        mongoTemplate.remove(query, ExternalTokenEntity.class);
    }
}
