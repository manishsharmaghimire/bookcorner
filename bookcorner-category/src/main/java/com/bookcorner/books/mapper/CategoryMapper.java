package com.bookcorner.books.mapper;

import com.bookcorner.books.dto.CategoryResponse;
import com.bookcorner.books.entity.Category;
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
