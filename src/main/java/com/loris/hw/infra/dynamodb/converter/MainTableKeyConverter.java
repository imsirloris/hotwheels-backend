package com.loris.hw.infra.dynamodb.converter;

import com.loris.hw.infra.dynamodb.model.MainTableKey;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClientExtension;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbExtensionContext;
import software.amazon.awssdk.enhanced.dynamodb.extensions.WriteModification;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class MainTableKeyConverter implements DynamoDbEnhancedClientExtension {

    @Override
    public WriteModification beforeWrite(DynamoDbExtensionContext.BeforeWrite context) {
        Object item = context.items();
        if (item instanceof MainTableKeyAware aware) {
            MainTableKey key = aware.mainTableKey();
            context.items().put("pk", AttributeValue.fromS(key.pk()));
            context.items().put("sk", AttributeValue.fromS(key.sk()));
        }
        return WriteModification.builder().build();
    }

    public interface MainTableKeyAware {
        MainTableKey mainTableKey();
    }
}