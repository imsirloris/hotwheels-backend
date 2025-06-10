package com.loris.hw.domain.port;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface StoragePort {
    Mono<String> upload(FilePart filePart, String targetName);
}