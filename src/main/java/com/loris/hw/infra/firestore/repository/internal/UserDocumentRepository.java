package com.loris.hw.infra.firestore.repository.internal;

import com.google.cloud.spring.data.firestore.FirestoreReactiveRepository;
import com.loris.hw.infra.firestore.document.UserDocument;

public interface UserDocumentRepository
        extends FirestoreReactiveRepository<UserDocument> { }