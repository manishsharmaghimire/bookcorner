package com.bookcorner.books.dto;

import com.bookcorner.books.enums.CategoryStatus;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CategoryResponse {

    private Long id;
    private String categoryName;
    private String categoryDescription;
    private CategoryStatus status;
}
