package com.loris.hw.adapter.web.dto.car;

import java.time.Year;

public record CarResponseDTO(
        String id,
        String modelName,
        String series,
        String seriesNumber,
        String color,
        Integer manufacturedYear,
        Integer purchaseYear,
        String batch,
        Boolean th,
        Boolean sth,
        String photoUrl,
        String ownerId
) { }
