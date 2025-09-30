package com.loris.hw.infra.aws;

import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;

import com.loris.hw.domain.port.StoragePort;

import reactor.core.publisher.Mono;

@Service
public class S3StorageService implements StoragePort {

    @Override
    public Mono<String> upload(FilePart filePart, String targetName) {
        // Implementação temporária que retorna uma URL mock
        // TODO: Implementar upload real para S3
        return Mono.just("https://mock-s3-bucket.s3.amazonaws.com/" + targetName);
    }

    @Override
    public Mono<Void> delete(String targetName) {
        // Implementação temporária
        // TODO: Implementar deleção real do S3
        return Mono.empty();
    }
}
