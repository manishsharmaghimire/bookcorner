package com.bookcorner.publisher.controller;

import com.bookcorner.publisher.dto.PublisherResponse;
import com.bookcorner.publisher.service.serviceimpl.PublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService publisherService;

    @GetMapping
    public ResponseEntity<List<PublisherResponse>> getAllActivePublishers() {
        List<PublisherResponse> responses = publisherService.getAllActivePublishers();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> getPublisherById(
            @PathVariable UUID id
    ) {
        PublisherResponse response = publisherService.getPublisherById(id);
        return ResponseEntity.ok(response);
    }
}
