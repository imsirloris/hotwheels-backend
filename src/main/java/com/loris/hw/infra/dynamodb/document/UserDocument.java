package com.loris.hw.infra.dynamodb.document;

import com.loris.hw.infra.dynamodb.model.MainTableKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamoDbBean
public class UserDocument {

    private String pk; // USER#123
    private String sk; // META#123
    private String type = "USER";

    private String id;
    private String username;
    private String email;

    @DynamoDbPartitionKey
    public String getPk() {
        return pk;
    }

    @DynamoDbSortKey
    public String getSk() {
        return sk;
    }

    public static UserDocument fromDomain(com.loris.hw.domain.model.User user) {
        UserDocument doc = new UserDocument();
        doc.setId(user.id());
        doc.setUsername(user.username());
        doc.setEmail(user.email());

        MainTableKey key = MainTableKey.userMeta(user.id());
        doc.setPk(key.pk());
        doc.setSk(key.sk());

        return doc;
    }

    public com.loris.hw.domain.model.User toDomain() {
        return new com.loris.hw.domain.model.User(
                this.id,
                this.username,
                this.email);
    }
}
