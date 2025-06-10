package com.loris.hw.domain.port.repository;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import com.loris.hw.infra.firestore.document.UserDocument;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends FirestoreReactiveRepository<UserDocument> {
    Mono<UserDocument> findByUsername(String username);
}