package com.loris.hw.adapter.web.dto.car;

public record CarCreateUpdateDTO(
        String modelName,
        String color,
        String series,
        String seriesNumber,
        Integer manufacturedYear,
        Integer purchaseYear,
        String batch,
        Boolean th,
        Boolean sth
) { }
