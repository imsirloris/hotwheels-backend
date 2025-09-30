package com.loris.hw.infra.dynamodb.repository;

import org.springframework.stereotype.Repository;

import com.loris.hw.infra.dynamodb.document.UserDocument;
import com.loris.hw.infra.dynamodb.model.MainTableKey;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements com.loris.hw.domain.port.repository.UserRepository {

    private final DynamoDbTable<UserDocument> userTable;

    @Override
    public Mono<UserDocument> findById(String id) {
        return Mono.fromCallable(() -> {
            MainTableKey key = MainTableKey.userMeta(id);
            Key dynamoKey = Key.builder()
                    .partitionValue(key.pk())
                    .sortValue(key.sk())
                    .build();
            return userTable.getItem(r -> r.key(dynamoKey));
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<UserDocument> save(UserDocument user) {
        return Mono.fromCallable(() -> {
            if (user.getPk() == null || user.getSk() == null) {
                MainTableKey key = MainTableKey.userMeta(user.getId());
                user.setPk(key.pk());
                user.setSk(key.sk());
            }
            userTable.putItem(user);
            return user;
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
