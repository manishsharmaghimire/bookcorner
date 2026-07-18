package com.bookcorner.books.dto;

import com.bookcorner.books.enums.CategoryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryStatusRequest {

    @NotNull(message = "Category status is required.")
    private CategoryStatus status;

}
