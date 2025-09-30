package com.loris.hw.infra.dynamodb.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.loris.hw.infra.dynamodb.document.CarDocument;
import com.loris.hw.infra.dynamodb.model.MainTableKey;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Expression;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.enhanced.dynamodb.model.ScanEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@Repository
@RequiredArgsConstructor
public class CarRepositoryImpl implements com.loris.hw.domain.port.repository.CarRepository {

    private final DynamoDbTable<CarDocument> table;

    @Override
    public Mono<CarDocument> save(CarDocument car) {
        return Mono.fromCallable(() -> {
            if (car.getId() == null) {
                car.setId(UUID.randomUUID().toString());
                MainTableKey key = MainTableKey.userCar(car.getOwnerId(), car.getId());
                car.setPk(key.pk());
                car.setSk(key.sk());
            }
            table.putItem(car);
            return car;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<CarDocument> findById(String carId) {
        return Mono.fromCallable(() -> {
            // Para buscar por ID, precisamos fazer um scan já que não temos o ownerId
            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":id", AttributeValue.builder().s(carId).build());
            expressionValues.put(":type", AttributeValue.builder().s("CAR").build());

            Map<String, String> expressionNames = new HashMap<>();
            expressionNames.put("#type", "type");

            Expression filterExpression = Expression.builder()
                    .expression("id = :id AND #type = :type")
                    .expressionValues(expressionValues)
                    .expressionNames(expressionNames)
                    .build();

            ScanEnhancedRequest request = ScanEnhancedRequest.builder()
                    .filterExpression(filterExpression)
                    .build();

            return table.scan(request).items().stream()
                    .findFirst()
                    .orElse(null);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<CarDocument> findByOwnerId(String ownerId) {
        return Mono.fromCallable(() -> {
            QueryConditional conditional = QueryConditional.keyEqualTo(
                    Key.builder().partitionValue("USER#" + ownerId).build());

            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":type", AttributeValue.builder().s("CAR").build());

            Map<String, String> expressionNames = new HashMap<>();
            expressionNames.put("#type", "type");

            Expression filterExpression = Expression.builder()
                    .expression("#type = :type")
                    .expressionValues(expressionValues)
                    .expressionNames(expressionNames)
                    .build();

            QueryEnhancedRequest request = QueryEnhancedRequest.builder()
                    .queryConditional(conditional)
                    .filterExpression(filterExpression)
                    .build();

            return table.query(request).items();
        })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Flux<CarDocument> findAll(int limit, String lastDocumentId) {
        return Mono.fromCallable(() -> {
            Map<String, AttributeValue> expressionValues = new HashMap<>();
            expressionValues.put(":type", AttributeValue.builder().s("CAR").build());

            Map<String, String> expressionNames = new HashMap<>();
            expressionNames.put("#type", "type");

            Expression filterExpression = Expression.builder()
                    .expression("#type = :type")
                    .expressionValues(expressionValues)
                    .expressionNames(expressionNames)
                    .build();

            ScanEnhancedRequest.Builder requestBuilder = ScanEnhancedRequest.builder()
                    .limit(limit)
                    .filterExpression(filterExpression);

            // Se há um lastDocumentId, usar como ponto de partida para paginação
            if (lastDocumentId != null && !lastDocumentId.isEmpty()) {
                // Em uma implementação real, você precisaria armazenar a chave completa para
                // paginação
                // Por simplicidade, vamos implementar sem exclusiveStartKey por enquanto
            }

            return table.scan(requestBuilder.build()).items();
        })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public Mono<Void> deleteById(String carId) {
        return findById(carId)
                .flatMap(car -> Mono.fromRunnable(() -> {
                    Key key = Key.builder()
                            .partitionValue(car.getPk())
                            .sortValue(car.getSk())
                            .build();
                    table.deleteItem(key);
                }).subscribeOn(Schedulers.boundedElastic()))
                .then();
    }
}