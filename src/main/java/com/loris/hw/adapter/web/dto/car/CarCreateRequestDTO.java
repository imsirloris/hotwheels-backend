package com.loris.hw.adapter.web.dto.car;

import org.springframework.http.codec.multipart.FilePart;

public record CarCreateRequestDTO(
        FilePart photo,
        String modelName,
        String series,
        String seriesNumber,
        Integer manufacturedYear,
        Integer purchaseYear,
        String batch,
        Boolean th,
        Boolean sth
) { }
