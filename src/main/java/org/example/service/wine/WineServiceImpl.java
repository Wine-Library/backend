package org.example.service.wine;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.example.dto.wine.WineRequestDto;
import org.example.dto.wine.WineResponseDto;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class WineServiceImpl implements WineService {
    private final WineRepository wineRepository;
    private final WineMapper wineMapper;
    private final ImportImageService importImageService;
    private final WineSpecificationBuilder wineSpecificationBuilder;

    @Value("${cloud.r2.public-url}")
    private String publicUrl;

    @Override
    @Transactional
    public WineResponseDto save(WineRequestDto requestDto) throws FileUploadException {
        Wine wine = wineMapper.toEntity(requestDto);
        wine.setProductImage("pending");
        wine = wineRepository.save(wine);

        try {
            String objectKey = importImageService.uploadFile(wine.getId(),
                    requestDto.getProductImage());
            wine.setProductImage(objectKey);
            wine = wineRepository.save(wine);

            WineResponseDto wineDto = wineMapper.toDto(wine);
            wineDto.setProductImage(publicUrl + "/" + wine.getProductImage());

            return wineDto;
        } catch (Exception e) {
            throw new FileUploadException("Failed to upload image for wine "
                    + requestDto);
        }
    }

    @Override
    public Page<WineResponseDto> getAll(Pageable pageable) {
        return wineRepository.findAll(pageable)
                .map(wineMapper::toDto)
                .map(w -> w.setProductImage(publicUrl + "/" + w.getProductImage()));
    }

    @Override
    public WineResponseDto getWineById(Long id) {
        Wine wine = wineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot find wine by id: " + id
                ));

        WineResponseDto wineDto = wineMapper.toDto(wine);
        wineDto.setProductImage(publicUrl + "/" + wine.getProductImage());

        return wineDto;
    }

    @Override
    @Transactional
    public WineResponseDto updateWineById(Long id, WineRequestDto requestDto)
            throws FileUploadException {
        Wine wine = wineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot update wine by id: " + id
                ));
        String previousProductImage = wine.getProductImage();

        if (requestDto.getProductImage() != null && !requestDto.getProductImage().isEmpty()) {
            try {
                String objectKey = importImageService.uploadFile(wine.getId(),
                        requestDto.getProductImage());
                wine.setProductImage(objectKey);
                importImageService.deleteFile(previousProductImage);
            } catch (Exception e) {
                throw new FileUploadException("Failed to upload image for wine " + id);
            }
        }

        wine.setWineName(requestDto.getWineName())
                .setWineType(requestDto.getWineType())
                .setCountryOfOrigin(requestDto.getCountryOfOrigin())
                .setPrice(requestDto.getPrice())
                .setPopularityRating(requestDto.getPopularityRating())
                .setOccasions(requestDto.getOccasions());

        wine = wineRepository.save(wine);

        WineResponseDto wineDto = wineMapper.toDto(wine);
        wineDto.setProductImage(publicUrl + "/" + wine.getProductImage());

        return wineDto;
    }

    @Override
    @Transactional
    public void deleteWineById(Long id) {
        Wine wine = wineRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cannot delete wine by id: " + id
                ));

        importImageService.deleteFile(wine.getProductImage());

        wineRepository.deleteById(id);
    }

    @Override
    @Transactional
    public WineResponseDto importImage(Long wineId, MultipartFile file)
            throws FileUploadException {
        Wine wine = wineRepository.findById(wineId)
                .orElseThrow(() -> new EntityNotFoundException(
                "Cannot find wine by id: " + wineId
        ));

        String previousProductImage = wine.getProductImage();

        try {
            String objectKey = importImageService.uploadFile(wine.getId(), file);

            wine.setProductImage(objectKey);
            wine = wineRepository.save(wine);

            importImageService.deleteFile(previousProductImage);

            WineResponseDto wineDto = wineMapper.toDto(wine);
            wineDto.setProductImage(publicUrl + "/" + wine.getProductImage());

            return wineDto;

        } catch (Exception e) {
            throw new FileUploadException("Failed to upload image for wine " + wineId);
        }
    }

    @Override
    public Page<WineResponseDto> search(WineSearchParameters searchParameters, Pageable pageable) {
        Specification<Wine> wineSpecification = wineSpecificationBuilder
                .buildSpecification(searchParameters);
        return wineRepository.findAll(wineSpecification, pageable)
                .map(wineMapper::toDto)
                .map(w -> w.setProductImage(publicUrl + "/" + w.getProductImage()));
    }
}
