package com.bookcorner.cart.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateCartItemRequest {

    @Min(value = 1, message = "Quantity must be at least 1.")
    private Integer quantity;
}
