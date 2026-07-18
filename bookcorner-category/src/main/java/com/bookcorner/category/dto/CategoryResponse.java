package com.bookcorner.category.dto;

import com.bookcorner.category.enums.CategoryStatus;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CategoryResponse {

    private UUID id;
    private String categoryName;
    private String categoryDescription;
    private CategoryStatus status;
}
