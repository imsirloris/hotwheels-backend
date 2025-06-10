package com.loris.hw.application.service;

import com.loris.hw.adapter.web.dto.car.CarCreateRequestDTO;
import com.loris.hw.adapter.web.dto.car.CarResponseDTO;
import com.loris.hw.adapter.web.mapper.CarMapper;
import com.loris.hw.domain.port.StoragePort;
import com.loris.hw.domain.port.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final StoragePort storage;
    private final CarMapper mapper;

    public Mono<CarResponseDTO> create(
            Mono<FilePart> photoMono,
            Mono<CarCreateRequestDTO> dataMono,
            String ownerId) {

        return Mono.zip(photoMono, dataMono)
                .flatMap(tuple -> {
                    FilePart photo = tuple.getT1();
                    CarCreateRequestDTO dto = tuple.getT2();

                    String fileName = UUID.randomUUID() + "-" + photo.filename();

                    return storage.upload(photo, fileName)
                            .map(url -> mapper.toDocument(dto, ownerId, url));
                })
                .flatMap(carRepository::save)
                .map(mapper::toResponseDto);
    }
}