package com.bookcorner.books.controller;

import com.bookcorner.books.dto.BookDetailsResponse;
import com.bookcorner.books.dto.BookListResponse;
import com.bookcorner.books.dto.BookRequest;
import com.bookcorner.books.dto.BookResponse;
import com.bookcorner.books.dto.BookSearchRequest;
import com.bookcorner.books.service.serviceimpl.BookService;
import com.bookcorner.shared.dto.PageResponse;
import com.bookcorner.shared.dto.PaginationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    public ResponseEntity<PageResponse<BookListResponse>> getAllActiveBooks(
            @ModelAttribute PaginationRequest request
    ) {

        return ResponseEntity.ok(bookService.getAllActiveBooks(request));
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<BookListResponse>> searchBooks(
            @ModelAttribute BookSearchRequest request
    ) {
        return ResponseEntity.ok(bookService.searchBooks(request));
    }


    @GetMapping("/{id}")
    public ResponseEntity<BookDetailsResponse> getBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@Valid @RequestBody BookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable UUID id, @Valid @RequestBody BookRequest request) {
        return ResponseEntity.ok(bookService.updateBooks(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
