package com.loris.hw.adapter.web.dto.car;

import java.util.List;

public record CarPageResponseDTO(
        List<CarResponseDTO> content,
        int size,
        String lastDocumentId,
        boolean hasMore
) {}