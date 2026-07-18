package com.bookcorner.publisher.repository;

import com.bookcorner.publisher.entity.Publisher;
import com.bookcorner.publisher.enums.PublisherStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PublisherRepository extends JpaRepository<Publisher, UUID> {

    boolean existByPublisherName(String publisherName);

    Optional<Publisher> findByPublisherName(String publisherName);

    List<Publisher> findByStatus(PublisherStatus status);
}
