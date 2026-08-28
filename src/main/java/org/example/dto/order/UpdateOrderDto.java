package org.example.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.example.model.Order;

@Getter
@Setter
@Accessors(chain = true)
public class UpdateOrderDto {

    @NotNull(message = "${validation.order-status.invalid}")
    private Order.Status status;

}
