package com.loris.hw.adapter.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loris.hw.adapter.web.dto.car.CarCreateRequestDTO;
import com.loris.hw.adapter.web.dto.car.CarResponseDTO;
import com.loris.hw.application.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
@Validated
public class CarController {

    private final CarService carService;

    @PostMapping(path = "/owner/{ownerId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CarResponseDTO>> create(
            @PathVariable String ownerId,
            @RequestPart("photo") Mono<FilePart> photo,
            @RequestPart("data") String jsonData) {

        ObjectMapper objectMapper = new ObjectMapper();
        return Mono.fromCallable(() -> objectMapper.readValue(jsonData, CarCreateRequestDTO.class))
                .flatMap(dto -> carService.create(photo, Mono.just(dto), ownerId))
                .map(car -> ResponseEntity
                        .created(URI.create("/api/v1/cars/" + car.id()))
                        .body(car));
    }
}
