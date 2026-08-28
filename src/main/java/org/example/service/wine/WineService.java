package org.example.service.wine;

import org.example.dto.wine.WineRequestDto;
import org.example.dto.wine.WineResponseDto;
import org.example.repository.filter.wine.WineSearchParameters;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface WineService {
    WineResponseDto save(WineRequestDto requestDto);

    Page<WineResponseDto> getAll(Pageable pageable);

    WineResponseDto getWineById(Long id);

    WineResponseDto updateWineById(Long id, WineRequestDto requestDto);

    void deleteWineById(Long id);

    WineResponseDto importImage(Long wineId, MultipartFile file);

    Page<WineResponseDto> search(WineSearchParameters searchParameters, Pageable pageable);
}
