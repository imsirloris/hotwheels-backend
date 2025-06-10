package com.loris.hw.domain.port.repository;

import com.loris.hw.infra.firestore.document.CarDocument;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CarRepository {
    Mono<CarDocument> save(CarDocument car);

    Mono<CarDocument> findById(String id);

    Flux<CarDocument> findByOwnerId(String ownerId);

    Mono<Void> deleteById(String id);
}