package com.loris.hw.adapter.web.dto.car;

import org.springframework.web.multipart.MultipartFile;

public record CarCreateRequestDTO(
        MultipartFile photo,
        String modelName,
        String series,
        String seriesNumber,
        Integer manufacturedYear,
        Integer purchaseYear,
        String batch,
        Boolean th,
        Boolean sth
) { }
