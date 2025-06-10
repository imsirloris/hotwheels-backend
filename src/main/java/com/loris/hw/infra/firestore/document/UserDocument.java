package com.loris.hw.infra.firestore.document;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.Data;

@Document(collectionName = "users")
@Data
public class UserDocument {
    private @DocumentId String id;
    private String username;
    private String email;
}