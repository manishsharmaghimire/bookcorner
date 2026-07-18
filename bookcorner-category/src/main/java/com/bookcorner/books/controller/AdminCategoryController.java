package com.bookcorner.books.controller;

import com.bookcorner.books.dto.CategoryRequest;
import com.bookcorner.books.dto.CategoryResponse;
import com.bookcorner.books.dto.CategoryStatusRequest;
import com.bookcorner.books.entity.Category;
import com.bookcorner.books.enums.CategoryStatus;
import com.bookcorner.books.exception.CategoryNotFoundException;
import com.bookcorner.books.mapper.CategoryMapper;
import com.bookcorner.books.repository.CategoryRepository;
import com.bookcorner.books.service.serviceimpl.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(
            @PathVariable UUID id
    ) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> responses = categories.stream()
                .map(categoryMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<CategoryResponse>> getCategoriesByStatus(
            @PathVariable CategoryStatus status
    ) {
        List<Category> categories = categoryRepository.findByStatus(status);
        List<CategoryResponse> responses = categories.stream()
                .map(categoryMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequest request
    ) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CategoryResponse> changeCategoryStatus(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryStatusRequest request
    ) {
        CategoryResponse response = categoryService.changeCategoryStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable UUID id
    ) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category not found."));
        categoryRepository.delete(category);
        return ResponseEntity.ok("Category deleted successfully.");
    }
}
