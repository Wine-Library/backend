package org.example.dto.wine;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class WineResponseDto {
    private Long id;
    private String wineName;
    private Double price;
    private String countryOfOrigin;
    private String wineType;
    private Double popularityRating;
    private String occasion;
    private String productImage;
}
