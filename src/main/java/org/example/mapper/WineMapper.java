package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.wine.WineRequestDto;
import org.example.dto.wine.WineResponseDto;
import org.example.model.Wine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface WineMapper {

    WineResponseDto toDto(Wine wine);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "productImage", ignore = true)
    Wine toEntity(WineRequestDto requestDto);
}
