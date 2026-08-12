package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.user.registration.UserRequestDto;
import org.example.dto.user.registration.UserResponseDto;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toEntity(UserRequestDto requestDto);
}
