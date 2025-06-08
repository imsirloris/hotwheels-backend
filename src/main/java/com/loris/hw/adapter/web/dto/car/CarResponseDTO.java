package com.loris.hw.adapter.web.dto.car;

public record CarResponseDTO(
        String id,
        String modelName,
        String series,
        Integer seriesNumber,
        Integer manufacturedYear,
        Integer purchaseYear,
        String batch,
        Boolean th,
        Boolean sth,
        String photoUrl,
        String ownerId
) { }
