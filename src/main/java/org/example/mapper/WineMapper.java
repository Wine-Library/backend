//package org.example.mapper;
//
//import org.example.dto.wine.WineRequestDto;
//import org.example.dto.wine.WineResponseDto;
//import org.example.model.Wine;
//import org.mapstruct.Mapping;
//
//public interface WineMapper {
//    WineResponseDto toDto(Wine wine);
//
//    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "deleted", ignore = true)
//    Wine toEntity(WineRequestDto requestDto);
//}
