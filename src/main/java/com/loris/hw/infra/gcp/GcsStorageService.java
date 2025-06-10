package com.loris.hw.infra.gcp;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.loris.hw.domain.port.StoragePort;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class GcsStorageService implements StoragePort {

    private final Storage storage;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucket;


    @Override
    public Mono<String> upload(FilePart filePart, String targetName) {

        return DataBufferUtils.join(filePart.content()).map(dataBuffer -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];

            dataBuffer.read(bytes);

            DataBufferUtils.release(dataBuffer);

            return bytes;
        }).flatMap(bytes -> Mono.fromCallable(() -> {
            BlobId blobId = BlobId.of(bucket, targetName);

            BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(filePart.headers().getContentType() != null
                    ? Objects.requireNonNull(filePart.headers().getContentType()).toString()
                    : "application/octet-stream").build();

            storage.create(blobInfo, bytes);

            return "https://storage.googleapis.com/%s/%s".formatted(bucket, targetName);
        }).subscribeOn(Schedulers.boundedElastic()));
    }

}
