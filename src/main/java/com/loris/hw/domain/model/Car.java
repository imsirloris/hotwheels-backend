package com.loris.hw.domain.model;

public record Car(
        String id,
        String modelName,
        String series,
        String seriesNumber,
        Integer manufacturedYear,
        Integer purchaseYear,
        String batch,
        Boolean th,
        Boolean sth,
        String photoUrl,
        String ownerId
) { }
