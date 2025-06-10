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
import reactor.core.publisher.Flux;
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

    @PutMapping(path = "/{carId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CarResponseDTO>> update(
            @PathVariable String carId,
            @RequestPart("photo") Mono<FilePart> photo,
            @RequestPart("data") String jsonData) {

        ObjectMapper objectMapper = new ObjectMapper();
        return Mono.fromCallable(() -> objectMapper.readValue(jsonData, CarCreateRequestDTO.class))
                .flatMap(dto -> carService.update(carId, photo, Mono.just(dto)))
                .map(car -> ResponseEntity
                        .ok()
                        .body(car));
    }

    @GetMapping(path = "/owner/{ownerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Flux<CarResponseDTO>>> findByOwnerId(@PathVariable String ownerId) {
        return carService.findByOwnerId(ownerId)
                .map(cars -> ResponseEntity.ok().body(cars));
    }

    @GetMapping(path = "/{carId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<CarResponseDTO>> findById(@PathVariable String carId) {
        return carService.findById(carId)
                .map(car -> ResponseEntity.ok().body(car))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping(path = "/{carId}")
    public Mono<ResponseEntity<Object>> deleteById(@PathVariable String carId) {
        return carService.deleteById(carId)
                .then(Mono.just(ResponseEntity.noContent().build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.notFound().build()));
    }

}
