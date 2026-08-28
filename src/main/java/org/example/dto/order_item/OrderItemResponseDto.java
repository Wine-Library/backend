package org.example.dto.order_item;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class OrderItemResponseDto {
    private Long id;
    private Long wineId;
    private int quantity;
    private BigDecimal price;
}
