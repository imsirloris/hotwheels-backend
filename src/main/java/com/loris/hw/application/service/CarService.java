package com.loris.hw.application.service;

import com.loris.hw.adapter.web.dto.car.CarCreateRequestDTO;
import com.loris.hw.adapter.web.dto.car.CarPageResponseDTO;
import com.loris.hw.adapter.web.dto.car.CarResponseDTO;
import com.loris.hw.adapter.web.mapper.CarMapper;
import com.loris.hw.domain.port.StoragePort;
import com.loris.hw.domain.port.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final StoragePort storage;
    private final CarMapper mapper;

    @Transactional
    public Mono<CarResponseDTO> create(
            Mono<FilePart> photoMono,
            Mono<CarCreateRequestDTO> dataMono,
            String ownerId) {

        int currentYear = java.time.Year.now().getValue();

        return Mono.zip(photoMono, dataMono)
                .flatMap(tuple -> {
                    FilePart photo = tuple.getT1();
                    CarCreateRequestDTO dto = tuple.getT2();

                    String folderPath = ownerId + "/" + currentYear + "/";
                    String fileName = folderPath + UUID.randomUUID() + "-" + photo.filename();

                    return storage.upload(photo, fileName)
                            .map(url -> mapper.toDocument(dto, ownerId, url));
                })
                .flatMap(carRepository::save)
                .map(mapper::toResponseDto);
    }

    @Transactional
    public Mono<CarResponseDTO> update(
            String carId,
            Mono<FilePart> photoMono,
            Mono<CarCreateRequestDTO> dataMono) {

        return carRepository.findById(carId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Car not found")))
                .flatMap(car -> Mono.zip(photoMono, dataMono)
                        .flatMap(tuple -> {
                            FilePart photo = tuple.getT1();
                            CarCreateRequestDTO dto = tuple.getT2();

                            if (photo != null) {
                                String folderPath = car.getOwnerId() + "/" + car.getManufacturedYear() + "/";
                                String fileName = folderPath + UUID.randomUUID() + "-" + photo.filename();
                                return storage.upload(photo, fileName)
                                        .map(url -> mapper.toDocument(dto, car.getOwnerId(), url));
                            } else {
                                return Mono.just(mapper.toDocument(dto, car.getOwnerId(), car.getPhotoUrl()));
                            }
                        }))
                .flatMap(carRepository::save)
                .map(mapper::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Mono<Flux<CarResponseDTO>> findByOwnerId(String ownerId) {
        return carRepository.findByOwnerId(ownerId)
                .collectList()
                .map(cars -> Flux.fromIterable(cars)
                        .map(mapper::toResponseDto));
    }

    @Transactional(readOnly = true)
    public Mono<CarResponseDTO> findById(String carId) {
        return carRepository.findById(carId)
                .map(mapper::toResponseDto)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Car not found")));
    }

    @Transactional(readOnly = true)
    public Mono<CarPageResponseDTO> findAllPaginated(int limit, String lastDocumentId) {
        return carRepository.findAll(limit + 1, lastDocumentId)
                .map(mapper::toResponseDto)
                .collectList()
                .map(list -> {
                    boolean hasMore = list.size() > limit;
                    List<CarResponseDTO> content = hasMore ? list.subList(0, limit) : list;
                    String lastId = !content.isEmpty() ? content.get(content.size() - 1).id() : null;

                    return new CarPageResponseDTO(
                            content,
                            content.size(),
                            lastId,
                            hasMore
                    );
                });
    }

    @Transactional
    public Mono<Void> deleteById(String carId) {
        return carRepository.findById(carId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Car not found")))
                .flatMap(car -> storage.delete(car.getPhotoUrl())
                        .then(carRepository.deleteById(carId)));
    }

}