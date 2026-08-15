package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.wine.WineRequestDto;
import org.example.dto.wine.WineResponseDto;
import org.example.service.wine.WineService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
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
    public WineResponseDto importImage(@RequestParam Long wineId,
                                       @RequestParam("imageFile") MultipartFile imageFile
    ) {
        return wineService.importImage(wineId, imageFile);
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
}
