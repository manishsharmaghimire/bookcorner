package com.bookcorner.author.controller;

import com.bookcorner.author.dto.AuthorRequest;
import com.bookcorner.author.dto.AuthorResponse;
import com.bookcorner.author.dto.AuthorStatusRequest;
import com.bookcorner.author.entity.Author;
import com.bookcorner.author.enums.AuthorStatus;
import com.bookcorner.author.exception.AuthorNotFoundException;
import com.bookcorner.author.mapper.AuthorMapper;
import com.bookcorner.author.repository.AuthorRepository;
import com.bookcorner.author.service.serviceimpl.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/authors")
@RequiredArgsConstructor
public class AdminAuthorController {

    private final AuthorService authorService;
    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    @PostMapping
    public ResponseEntity<AuthorResponse> createAuthor(
            @Valid @RequestBody AuthorRequest request
    ) {
        AuthorResponse response = authorService.createAuthor(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(
            @PathVariable UUID id
    ) {
        AuthorResponse response = authorService.getAuthorById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorResponse> responses = authors.stream()
                .map(authorMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<AuthorResponse>> getAuthorsByStatus(
            @PathVariable AuthorStatus status
    ) {
        List<Author> authors = authorRepository.findByStatus(status);
        List<AuthorResponse> responses = authors.stream()
                .map(authorMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(
            @PathVariable UUID id,
            @Valid @RequestBody AuthorRequest request
    ) {
        AuthorResponse response = authorService.updateAuthor(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<AuthorResponse> changeAuthorStatus(
            @PathVariable UUID id,
            @Valid @RequestBody AuthorStatusRequest request
    ) {
        AuthorResponse response = authorService.changeAuthorStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(
            @PathVariable UUID id
    ) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found."));
        authorRepository.delete(author);
        return ResponseEntity.ok("Author deleted successfully.");
    }
}
