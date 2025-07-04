package com.loris.hw.infra.dynamodb.config;

import com.loris.hw.infra.dynamodb.converter.MainTableKeyConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.net.URI;

@Configuration
@EnableConfigurationProperties(DynamoDbConfig.DynamoDbConfigProperties.class)
public class DynamoDbConfig {

    private final DynamoDbConfigProperties props;

    public DynamoDbConfig(DynamoDbConfigProperties props) {
        this.props = props;
    }

    @Bean
    DynamoDbClient dynamoDbClient() {
        return DynamoDbClient.builder()
                .region(Region.of(props.region()))
                .endpointOverride(props.dynamodb().endpoint() != null
                        ? URI.create(props.dynamodb().endpoint())
                        : null)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    @Bean
    DynamoDbEnhancedClient enhancedClient(
            DynamoDbClient client,
            MainTableKeyConverter keyConverter) {

        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(client)
                .extensions(keyConverter)
                .build();
    }

    @Bean
    MainTableKeyConverter keyConverter() {
        return new MainTableKeyConverter();
    }

    @ConfigurationProperties("aws")
    public static record DynamoDbConfigProperties(String region, DynamoDbProps dynamodb) {
        public static record DynamoDbProps(String endpoint, String tableName) {
        }
    }
}