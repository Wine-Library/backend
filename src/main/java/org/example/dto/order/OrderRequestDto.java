package org.example.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OrderRequestDto {
    @NotBlank(message = "${validation.street.invalid}")
    private String street;

    @NotBlank(message = "${validation.city.invalid}")
    private String city;

    @NotBlank(message = "${validation.zip-code.invalid}")
    private String zipCode;
}
