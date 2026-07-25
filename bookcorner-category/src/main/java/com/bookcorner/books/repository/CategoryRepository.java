package com.bookcorner.books.repository;

import com.bookcorner.books.entity.Category;
import com.bookcorner.books.enums.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsByCategoryName(String categoryName);

    Optional<Category> findByCategoryName(String categoryName);

    List<Category> findByStatus(CategoryStatus status);
}
