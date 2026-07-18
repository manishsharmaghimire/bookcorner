package com.bookcorner.category.repository;

import com.bookcorner.category.entity.Category;
import com.bookcorner.category.enums.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existByCategoryName(String categoryName);

    Optional<Category> findByCategoryName(String categoryName);

    List<Category> findByStatus(CategoryStatus status);
}
