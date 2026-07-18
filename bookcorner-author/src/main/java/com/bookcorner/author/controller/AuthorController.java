package com.bookcorner.author.controller;

import com.bookcorner.author.dto.AuthorResponse;
import com.bookcorner.author.service.serviceimpl.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorResponse>> getAllActiveAuthors() {
        List<AuthorResponse> responses = authorService.getAllActiveAuthors();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(
            @PathVariable UUID id
    ) {
        AuthorResponse response = authorService.getAuthorById(id);
        return ResponseEntity.ok(response);
    }
}
