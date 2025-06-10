package com.loris.hw.infra.firestore.repository.internal;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import com.loris.hw.infra.firestore.document.CarDocument;
import reactor.core.publisher.Flux;

public interface CarDocumentRepository extends FirestoreReactiveRepository<CarDocument> {
    Flux<CarDocument> findByOwnerId(String ownerId);
}
