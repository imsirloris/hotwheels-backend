package com.loris.hw.infra.firestore.document;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.Data;

@Document(collectionName = "cars")
@Data
public class CarDocument {
    @DocumentId
    private String id;
    private String modelName;
    private String series;
    private String seriesNumber;
    private Integer manufacturedYear;
    private Integer purchaseYear;
    private String batch;
    private Boolean th;
    private Boolean sth;
    private String photoUrl;
    private String ownerId;

}