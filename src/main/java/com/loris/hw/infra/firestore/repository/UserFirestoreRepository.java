package com.loris.hw.infra.firestore.repository;

import com.loris.hw.domain.port.repository.UserRepository;
import com.loris.hw.infra.firestore.document.UserDocument;
import com.loris.hw.infra.firestore.repository.internal.UserDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class UserFirestoreRepository implements UserRepository {

    private final UserDocumentRepository repository;

    @Override
    public Mono<UserDocument> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found with id: " + id)));
    }

    @Override
    public Mono<UserDocument> save(UserDocument user) {
        return repository.save(user)
                .onErrorMap(e -> new RuntimeException("Error saving user: " + e.getMessage()));
    }
}