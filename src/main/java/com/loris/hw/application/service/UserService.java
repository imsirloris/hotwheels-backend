package com.loris.hw.application.service;

import com.loris.hw.adapter.web.dto.user.UserResponseDTO;
import com.loris.hw.adapter.web.dto.user.UserUpdateRequestDTO;
import com.loris.hw.adapter.web.mapper.UserMapper;
import com.loris.hw.domain.model.User;
import com.loris.hw.domain.port.repository.UserRepository;
import com.loris.hw.infra.firestore.document.UserDocument;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
    private final UserMapper mapper;

    public Mono<UserResponseDTO> findOrCreate(Jwt jwt) {

        String uid = jwt.getSubject();

        return repo.findById(uid)
                .switchIfEmpty(createFromJwt(jwt))
                .map(mapper::toDomain)
                .map(mapper::toDto);
    }

    public Mono<UserResponseDTO> update(String uid, UserUpdateRequestDTO dto) {

        return repo.findById(uid)
                .map(mapper::toDomain)
                .flatMap(existing -> mergeAndSave(existing, dto))
                .map(mapper::toDomain)
                .map(mapper::toDto);
    }


    private Mono<UserDocument> createFromJwt(Jwt jwt) {
        User newUser = mapper.jwtToDomain(jwt);
        UserDocument doc = mapper.toDocument(newUser);
        return repo.save(doc);
    }

    private Mono<UserDocument> mergeAndSave(User current, UserUpdateRequestDTO dto) {

        User merged = new User(
                current.id(),
                dto.username() != null ? dto.username() : current.username(),
                dto.email() != null ? dto.email() : current.email()
        );

        return repo.save(mapper.toDocument(merged));
    }
}
