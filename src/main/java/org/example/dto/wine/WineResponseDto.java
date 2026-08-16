package org.example.dto.wine;

import java.util.List;
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
    private List<String> occasions;
    private String productImage;
    private Integer year;
}
