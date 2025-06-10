package com.loris.hw.domain.port.repository;

import com.loris.hw.infra.firestore.document.UserDocument;
import reactor.core.publisher.Mono;

public interface UserRepository{
    Mono<UserDocument> findById(String id);
    Mono<UserDocument> save(UserDocument user);
}