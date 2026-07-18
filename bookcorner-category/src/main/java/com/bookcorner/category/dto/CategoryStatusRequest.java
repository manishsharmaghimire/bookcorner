package com.bookcorner.category.dto;

import com.bookcorner.category.enums.CategoryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryStatusRequest {

    @NotNull(message = "Category status is required.")
    private CategoryStatus status;

}
