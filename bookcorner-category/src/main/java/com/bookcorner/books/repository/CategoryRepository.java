package com.bookcorner.books.repository;

import com.bookcorner.books.entity.Category;
import com.bookcorner.books.enums.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    boolean existByCategoryName(String categoryName);

    Optional<Category> findByCategoryName(String categoryName);

    List<Category> findByStatus(CategoryStatus status);
}
