package com.loris.hw.infra.dynamodb.repository;

import com.loris.hw.infra.dynamodb.model.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CarRepository {

    private final DynamoDbTable<Car> table;

    public void save(Car car) {
        table.putItem(car);
    }

    public Optional<Car> findById(String userId, String carId) {
        Key key = Key.builder()
                .partitionValue("USER#" + userId)
                .sortValue("CAR#" + carId)
                .build();
        return Optional.ofNullable(table.getItem(r -> r.key(key)));
    }

    public SdkIterable<Car> listByUser(String userId) {
        return table.query(r -> r.queryConditional(
                        QueryConditional.keyEqualTo(k -> k.partitionValue("USER#" + userId))))
                .items();
    }
}