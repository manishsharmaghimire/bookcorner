package com.bookcorner.publisher.repository;

import com.bookcorner.publisher.entity.Publisher;
import com.bookcorner.publisher.enums.PublisherStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PublisherRepository extends JpaRepository<Publisher, Long> {

    boolean existsByPublisherName(String publisherName);

    Optional<Publisher> findByPublisherName(String publisherName);

    List<Publisher> findByStatus(PublisherStatus status);
}
