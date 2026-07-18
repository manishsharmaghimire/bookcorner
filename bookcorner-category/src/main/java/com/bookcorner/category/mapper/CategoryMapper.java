package com.bookcorner.category.mapper;

import com.bookcorner.category.dto.CategoryResponse;
import com.bookcorner.category.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getCategoryName(),
                category.getCategoryDescription(),
                category.getStatus()
        );
    }
}
