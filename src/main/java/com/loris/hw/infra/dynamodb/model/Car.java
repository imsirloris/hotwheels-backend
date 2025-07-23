package com.loris.hw.infra.dynamodb.model;

import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
public class Car {

    private String pk;          // USER#123
    private String sk;          // CAR#456
    private String type = "CAR";

    private String modelName;
    private String color;
    private String series;
    private String seriesNumber;
    private Integer manufacturedYear;
    private Integer purchaseYear;
    private String batch;
    private Boolean th;
    private Boolean sth;
    private String photoUrl;

    @DynamoDbPartitionKey
    public String getPk() { return pk; }

    @DynamoDbSortKey
    public String getSk() { return sk; }

}