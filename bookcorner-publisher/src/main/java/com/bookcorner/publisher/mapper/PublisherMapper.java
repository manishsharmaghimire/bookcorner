package com.bookcorner.publisher.mapper;

import com.bookcorner.publisher.dto.PublisherResponse;
import com.bookcorner.publisher.entity.Publisher;
import org.springframework.stereotype.Component;

@Component
public class PublisherMapper {

    public PublisherResponse toResponse(Publisher publisher) {
        return new PublisherResponse(
                publisher.getId(),
                publisher.getPublisherName(),
                publisher.getPublisherAddress(),
                publisher.getPublisherContact(),
                publisher.getStatus()
        );
    }
}
