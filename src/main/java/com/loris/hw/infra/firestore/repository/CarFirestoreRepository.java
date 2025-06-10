package com.loris.hw.infra.firestore.repository;

import com.loris.hw.adapter.web.mapper.CarMapper;
import com.loris.hw.domain.port.repository.CarRepository;
import com.loris.hw.infra.firestore.document.CarDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class CarFirestoreRepository implements CarRepository {

    private final CarMapper mapper;
    private final CarDocumentRepository firestore;

    @Override
    public Mono<CarDocument> save(CarDocument car) {
        return firestore.save(car);
    }

    @Override
    public Mono<CarDocument> findById(String id) {
        return firestore.findById(id);
    }

    @Override
    public Flux<CarDocument> findByOwnerId(String ownerId) {
        return firestore.findByOwnerId(ownerId);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return firestore.deleteById(id);
    }
}