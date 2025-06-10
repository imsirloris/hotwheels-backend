package com.loris.hw.adapter.web.controller;

import com.loris.hw.adapter.web.dto.user.UserResponseDTO;
import com.loris.hw.adapter.web.dto.user.UserUpdateRequestDTO;
import com.loris.hw.application.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/me")
    public Mono<ResponseEntity<UserResponseDTO>> getMe(@AuthenticationPrincipal Jwt jwt) {
        return service.findOrCreate(jwt)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/me")
    public Mono<ResponseEntity<UserResponseDTO>> updateMe(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserUpdateRequestDTO dto) {

        return service.update(jwt.getSubject(), dto)
                .map(ResponseEntity::ok);
    }
}
