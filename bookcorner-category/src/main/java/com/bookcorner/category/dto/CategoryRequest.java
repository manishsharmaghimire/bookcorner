package com.bookcorner.category.dto;

import com.bookcorner.category.enums.CategoryStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryRequest {

    @NotBlank(message = "Category name is required.")
    @Size(max = 100, message = "Category name must not exceed 100 characters.")
    private String categoryName;

    @Size(max = 500, message = "Category description must not exceed 500 characters.")
    private String categoryDescription;

}
