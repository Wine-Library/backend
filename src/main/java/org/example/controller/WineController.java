package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.wine.WineRequestDto;
import org.example.dto.wine.WineResponseDto;
import org.example.repository.filter.wine.WineSearchParameters;
import org.example.service.wine.WineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Wine management",
        description = "Endpoints for managing wines")
@RequiredArgsConstructor
@RestController
@RequestMapping("/wines")
public class WineController {

    private final WineService wineService;

    @GetMapping
    @Operation(
            summary = "Get all wines",
            description = "Get a paginated list of all available wines"
    )
    public Page<WineResponseDto> getAll(Pageable pageable) {
        return wineService.getAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get a wine",
            description = "Get an existing wine by its id"
    )
    public WineResponseDto getWineById(@PathVariable Long id) {
        return wineService.getWineById(id);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new wine",
            description = "Create a new wine"
    )
    public WineResponseDto createWine(
            @ModelAttribute @Valid WineRequestDto requestDto
    ) {
        return wineService.save(requestDto);
    }

    @PostMapping(path = "/import-image/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Import image for the wine",
            description = "Import image for the wine by its id"
    )
    @ResponseStatus(HttpStatus.CREATED)
    public WineResponseDto importImage(@PathVariable Long id,
                                       @RequestParam("imageFile") MultipartFile imageFile
    ) {
        return wineService.importImage(id, imageFile);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Update a wine",
            description = "Update an existing wine by its id"
    )
    public WineResponseDto updateWineById(
            @PathVariable Long id,
            @ModelAttribute @Valid WineRequestDto requestDto
    ) {
        return wineService.updateWineById(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
            summary = "Delete a wine",
            description = "Delete an existing wine by its id"
    )
    public void deleteWineById(@PathVariable Long id) {
        wineService.deleteWineById(id);
    }

    @GetMapping("/search")
    @Operation(summary = "Search wines",
            description = "Get a list of all available wines by certain parameter")
    public Page<WineResponseDto> search(WineSearchParameters searchParameters, Pageable pageable) {
        return wineService.search(searchParameters, pageable);
    }
}
