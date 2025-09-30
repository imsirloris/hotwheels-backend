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
public class CarDocument {

    private String pk; // USER#123
    private String sk; // CAR#456
    private String type = "CAR";

    private String id;
    private String modelName;
    private String series;
    private String seriesNumber;
    private String color;
    private Integer manufacturedYear;
    private Integer purchaseYear;
    private String batch;
    private Boolean th;
    private Boolean sth;
    private String photoUrl;
    private String ownerId;

    @DynamoDbPartitionKey
    public String getPk() {
        return pk;
    }

    @DynamoDbSortKey
    public String getSk() {
        return sk;
    }

    public static CarDocument fromDomain(com.loris.hw.domain.model.Car car) {
        CarDocument doc = new CarDocument();
        doc.setId(car.id());
        doc.setModelName(car.modelName());
        doc.setSeries(car.series());
        doc.setSeriesNumber(car.seriesNumber());
        doc.setColor(car.color());
        doc.setManufacturedYear(car.manufacturedYear());
        doc.setPurchaseYear(car.purchaseYear());
        doc.setBatch(car.batch());
        doc.setTh(car.th());
        doc.setSth(car.sth());
        doc.setPhotoUrl(car.photoUrl());
        doc.setOwnerId(car.ownerId());

        MainTableKey key = MainTableKey.userCar(car.ownerId(), car.id());
        doc.setPk(key.pk());
        doc.setSk(key.sk());

        return doc;
    }

    public com.loris.hw.domain.model.Car toDomain() {
        return new com.loris.hw.domain.model.Car(
                this.id,
                this.modelName,
                this.series,
                this.seriesNumber,
                this.color,
                this.manufacturedYear,
                this.purchaseYear,
                this.batch,
                this.th,
                this.sth,
                this.photoUrl,
                this.ownerId);
    }
}
