package org.example.service.wine;

import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.example.dto.wine.WineRequestDto;
import org.example.dto.wine.WineResponseDto;
import jakarta.persistence.EntityNotFoundException;
import org.example.mapper.WineMapper;
import org.example.model.Wine;
import org.example.repository.WineRepository;
import org.example.repository.filter.wine.WineSearchParameters;
import org.example.repository.filter.wine.WineSpecificationBuilder;
import org.example.service.wine.import_image.ImportImageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class WineServiceImpl implements WineService {
    private final WineRepository wineRepository;
    private final WineMapper wineMapper;
    private final ImportImageService importImageService;
    private final WineSpecificationBuilder wineSpecificationBuilder;

    @Override
    public WineResponseDto save(WineRequestDto requestDto) {
        Wine wine = wineMapper.toEntity(requestDto);
        try {
            String imageUrl = importImageService.uploadFile(requestDto.getProductImage());
            wine.setProductImage(imageUrl);
            wine = wineRepository.save(wine);

            return wineMapper.toDto(wine);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public Page<WineResponseDto> getAll(Pageable pageable) {
        return wineRepository.findAll(pageable)
                .map(wineMapper::toDto);
    }

    @Override
    public WineResponseDto getWineById(Long id) {
        Wine wine = wineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find wine by id: " + id
                ));

        return wineMapper.toDto(wine);
    }

    @Override
    public WineResponseDto updateWineById(Long id, WineRequestDto requestDto) {
        Wine wine = wineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot update wine by id: " + id
                ));

        if (requestDto.getProductImage() != null && !requestDto.getProductImage().isEmpty()) {
            try {
                String imageUrl = importImageService.uploadFile(requestDto.getProductImage());
                wine.setProductImage(imageUrl);
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload image", e);
            }
        }

        wine.setWineName(requestDto.getWineName())
                .setWineType(requestDto.getWineType())
                .setCountryOfOrigin(requestDto.getCountryOfOrigin())
                .setPrice(requestDto.getPrice())
                .setPopularityRating(requestDto.getPopularityRating())
                .setOccasions(requestDto.getOccasions());

        wine = wineRepository.save(wine);
        return wineMapper.toDto(wine);
    }

    @Override
    public void deleteWineById(Long id) {
        wineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot delete wine by id: " + id
                ));

        wineRepository.deleteById(id);
    }

    @Override
    public WineResponseDto importImage(Long wineId, MultipartFile file) {
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new EntityNotFoundException(
                "Cannot find wine by id: " + wineId
        ));
        try {
            String imageUrl = importImageService.uploadFile(file);
            wine.setProductImage(imageUrl);
            wine = wineRepository.save(wine);

            return wineMapper.toDto(wine);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    @Override
    public Page<WineResponseDto> search(WineSearchParameters searchParameters, Pageable pageable) {
        Specification<Wine> wineSpecification = wineSpecificationBuilder
                .buildSpecification(searchParameters);
        return wineRepository.findAll(wineSpecification, pageable)
                .map(wineMapper::toDto);
    }
}
