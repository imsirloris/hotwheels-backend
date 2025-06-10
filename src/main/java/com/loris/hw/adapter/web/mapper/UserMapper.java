package com.loris.hw.adapter.web.mapper;

import com.loris.hw.adapter.web.dto.user.UserResponseDTO;
import com.loris.hw.domain.model.User;
import com.loris.hw.infra.firestore.document.UserDocument;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserResponseDTO toDto(User user);

    UserDocument toDocument(User user);

    User toDomain(UserDocument doc);

    @Mapping(target = "id", source = "subject")
    @Mapping(target = "username", expression = "java(jwt.getClaimAsString(\"name\"))")
    @Mapping(target = "email", expression = "java(jwt.getClaimAsString(\"email\"))")
    User jwtToDomain(org.springframework.security.oauth2.jwt.Jwt jwt);

}