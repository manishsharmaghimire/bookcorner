package com.bookcorner.author.repository;

import com.bookcorner.author.entity.Author;
import com.bookcorner.author.enums.AuthorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository extends JpaRepository<Author, UUID> {

    boolean existByAuthorName(String authorName);

    Optional<Author> findByAuthorName(String authorName);

    List<Author> findByStatus(AuthorStatus status);
}
