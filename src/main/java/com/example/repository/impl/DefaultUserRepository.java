package com.example.repository.impl;

import com.example.domain.Message;
import com.example.domain.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class DefaultUserRepository implements UserRepository {

    private final MongoOperations mongoOperations;

    @Autowired
    DefaultUserRepository(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Optional<User> findMostActive() {
        return Optional.ofNullable(mongoOperations
                .aggregate(Aggregation.newAggregation(
                        Aggregation.group("user.id")
                                .addToSet("user.name").as("name")
                                .addToSet("user.displayName").as("displayName")
                                .count().as("popularity"),
                        Aggregation.sort(new Sort(new Sort.Order(Sort.Direction.DESC, "popularity"))),
                        Aggregation.limit(1),
                        Aggregation.unwind("name"),
                        Aggregation.unwind("displayName")
                ), Message.class, User.class)
                .getUniqueMappedResult());
    }

    @Override
    public Optional<User> findMostPopular() {
        return Optional.ofNullable(mongoOperations
                .aggregate(Aggregation.newAggregation(
                        Aggregation.unwind("mentions"),
                        Aggregation.group("mentions.userId")
                                .addToSet("mentions.screenName").as("name")
                                .addToSet("mentions.screenName").as("displayName")
                                .count().as("popularity"),
                        Aggregation.sort(new Sort(new Sort.Order(Sort.Direction.DESC, "popularity"))),
                        Aggregation.limit(1),
                        Aggregation.unwind("name"),
                        Aggregation.unwind("displayName")
                ), Message.class, User.class)
                .getUniqueMappedResult());
    }
}
