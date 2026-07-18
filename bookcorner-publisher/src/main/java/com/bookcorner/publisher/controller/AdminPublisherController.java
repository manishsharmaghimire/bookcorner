package com.bookcorner.publisher.controller;

import com.bookcorner.publisher.dto.PublisherRequest;
import com.bookcorner.publisher.dto.PublisherResponse;
import com.bookcorner.publisher.dto.PublisherStatusRequest;
import com.bookcorner.publisher.entity.Publisher;
import com.bookcorner.publisher.enums.PublisherStatus;
import com.bookcorner.publisher.exception.PublisherNotFoundException;
import com.bookcorner.publisher.mapper.PublisherMapper;
import com.bookcorner.publisher.repository.PublisherRepository;
import com.bookcorner.publisher.service.serviceimpl.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/publishers")
@RequiredArgsConstructor
public class AdminPublisherController {

    private final PublisherService publisherService;
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    @PostMapping
    public ResponseEntity<PublisherResponse> createPublisher(
            @Valid @RequestBody PublisherRequest request
    ) {
        PublisherResponse response = publisherService.createPublisher(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> getPublisherById(
            @PathVariable UUID id
    ) {
        PublisherResponse response = publisherService.getPublisherById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PublisherResponse>> getAllPublishers() {
        List<Publisher> publishers = publisherRepository.findAll();
        List<PublisherResponse> responses = publishers.stream()
                .map(publisherMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PublisherResponse>> getPublishersByStatus(
            @PathVariable PublisherStatus status
    ) {
        List<Publisher> publishers = publisherRepository.findByStatus(status);
        List<PublisherResponse> responses = publishers.stream()
                .map(publisherMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponse> updatePublisher(
            @PathVariable UUID id,
            @Valid @RequestBody PublisherRequest request
    ) {
        PublisherResponse response = publisherService.updatePublisher(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PublisherResponse> changePublisherStatus(
            @PathVariable UUID id,
            @Valid @RequestBody PublisherStatusRequest request
    ) {
        PublisherResponse response = publisherService.changePublisherStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePublisher(
            @PathVariable UUID id
    ) {
        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new PublisherNotFoundException("Publisher not found."));
        publisherRepository.delete(publisher);
        return ResponseEntity.ok("Publisher deleted successfully.");
    }
}
