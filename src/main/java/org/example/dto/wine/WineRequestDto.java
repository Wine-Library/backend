package org.example.dto.wine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Accessors(chain = true)
public class WineRequestDto {
    @NotBlank
    private String wineName;
    @Positive
    @NotNull
    private Double price;
    @NotBlank
    private String countryOfOrigin;
    @NotBlank
    private String wineType;
    @NotNull
    private Double popularityRating;
    @NotBlank
    private String occasion;
    @NotNull
    private MultipartFile productImage;
}
