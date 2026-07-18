package com.bookcorner.books.service.serviceimpl;


import com.bookcorner.books.dto.CategoryRequest;
import com.bookcorner.books.dto.CategoryResponse;
import com.bookcorner.books.dto.CategoryStatusRequest;
import com.bookcorner.books.entity.Category;
import com.bookcorner.books.enums.CategoryStatus;
import com.bookcorner.books.exception.CategoryAlreadyExistsException;
import com.bookcorner.books.exception.CategoryNotFoundException;
import com.bookcorner.books.mapper.CategoryMapper;
import com.bookcorner.books.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {

   private final CategoryRepository categoryRepository;
   private final CategoryMapper categoryMapper;


   public CategoryResponse createCategory(CategoryRequest categoryRequest) {

       if(categoryRepository.existByCategoryName(categoryRequest.getCategoryName())) {
           throw new CategoryAlreadyExistsException();
       }


       Category category = new Category();
       category.setCategoryName(categoryRequest.getCategoryName());
       category.setCategoryDescription(categoryRequest.getCategoryDescription());
       category.setStatus(CategoryStatus.ACTIVE);
       Category save = categoryRepository.save(category);
       return categoryMapper.toResponse(save);

   }
    public CategoryResponse getCategoryById(UUID id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new CategoryNotFoundException(
                                "Category not found."
                        )
                );

        return categoryMapper.toResponse(category);
    }

    public List<CategoryResponse> getAllActiveCategories() {

        List<Category> categories = categoryRepository.findByStatus(
                CategoryStatus.ACTIVE
        );

        return categories.stream()
                .map(categoryMapper::toResponse)
                .toList();
    }


    public CategoryResponse updateCategory(
            UUID id,
            CategoryRequest request
    ) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(
                        () -> new CategoryNotFoundException(
                                "Category not found."
                        )
                );

        Category existingCategory = categoryRepository
                .findByCategoryName(request.getCategoryName())
                .orElse(null);

        if (existingCategory != null &&
                !existingCategory.getId().equals(id)) {

            throw new CategoryAlreadyExistsException(
                    "Category already exists."
            );
        }

        category.setCategoryName(request.getCategoryName());
        category.setCategoryDescription(request.getCategoryDescription());

        Category updatedCategory =
                categoryRepository.save(category);

        return categoryMapper.toResponse(updatedCategory);
    }

    public CategoryResponse changeCategoryStatus(
            UUID id,
            CategoryStatusRequest request
    ) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new CategoryNotFoundException(
                                "Category not found."
                        )
                );

        category.setStatus(request.getStatus());

        categoryRepository.save(category);

        return categoryMapper.toResponse(category);
    }
}
