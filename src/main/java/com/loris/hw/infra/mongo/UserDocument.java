package com.loris.hw.infra.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@Data
public class UserDocument {
    @Id
    String id;
    String username;
    String email;
    String passwordHash;
}