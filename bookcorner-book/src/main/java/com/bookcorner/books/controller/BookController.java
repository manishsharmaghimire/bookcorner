package com.bookcorner.books.controller;

import com.bookcorner.books.dto.BookDetailsResponse;
import com.bookcorner.books.dto.BookListResponse;
import com.bookcorner.books.service.serviceimpl.BookService;
import com.bookcorner.shared.dto.PageResponse;
import com.bookcorner.shared.dto.PaginationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
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


    @GetMapping("/{id}")
    public ResponseEntity<BookDetailsResponse> getBookById(@PathVariable UUID id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
}
