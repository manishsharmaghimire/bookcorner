package com.bookcorner.cart.dto;


import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddQuantityRequest {
    @Min(1)
    private Integer quantity;
}
