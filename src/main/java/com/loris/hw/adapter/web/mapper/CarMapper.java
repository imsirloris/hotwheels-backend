package com.loris.hw.adapter.web.mapper;

import com.loris.hw.adapter.web.dto.car.CarCreateRequestDTO;
import com.loris.hw.adapter.web.dto.car.CarResponseDTO;
import com.loris.hw.domain.model.Car;
import com.loris.hw.infra.firestore.document.CarDocument;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CarMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "photoUrl", source = "photoUrl")
    CarDocument toDocument(CarCreateRequestDTO dto, String ownerId, String photoUrl);

    Car toDomain(CarDocument doc);

    CarResponseDTO toResponseDto(CarDocument doc);

//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
//    void updateDocumentFromDto(CarCreateUpdateDTO dto, @MappingTarget CarDocument targer);
}
