package com.bookcorner.publisher.service.serviceimpl;


import com.bookcorner.publisher.dto.PublisherRequest;
import com.bookcorner.publisher.dto.PublisherResponse;
import com.bookcorner.publisher.dto.PublisherStatusRequest;
import com.bookcorner.publisher.entity.Publisher;
import com.bookcorner.publisher.enums.PublisherStatus;
import com.bookcorner.publisher.exception.PublisherAlreadyExistsException;
import com.bookcorner.publisher.exception.PublisherNotFoundException;
import com.bookcorner.publisher.mapper.PublisherMapper;
import com.bookcorner.publisher.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PublisherService {

   private final PublisherRepository publisherRepository;
   private final PublisherMapper publisherMapper;


   public PublisherResponse createPublisher(PublisherRequest publisherRequest) {

       if(publisherRepository.existByPublisherName(publisherRequest.getPublisherName())) {
           throw new PublisherAlreadyExistsException();
       }


       Publisher publisher = new Publisher();
       publisher.setPublisherName(publisherRequest.getPublisherName());
       publisher.setPublisherAddress(publisherRequest.getPublisherAddress());
       publisher.setPublisherContact(publisherRequest.getPublisherContact());
       publisher.setStatus(PublisherStatus.ACTIVE);
       Publisher save = publisherRepository.save(publisher);
       return publisherMapper.toResponse(save);

   }
    public PublisherResponse getPublisherById(UUID id) {

        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(
                        () -> new PublisherNotFoundException(
                                "Publisher not found."
                        )
                );

        return publisherMapper.toResponse(publisher);
    }

    public List<PublisherResponse> getAllActivePublishers() {

        List<Publisher> publishers = publisherRepository.findByStatus(
                PublisherStatus.ACTIVE
        );

        return publishers.stream()
                .map(publisherMapper::toResponse)
                .toList();
    }


    public PublisherResponse updatePublisher(
            UUID id,
            PublisherRequest request
    ) {

        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(
                        () -> new PublisherNotFoundException(
                                "Publisher not found."
                        )
                );

        Publisher existingPublisher = publisherRepository
                .findByPublisherName(request.getPublisherName())
                .orElse(null);

        if (existingPublisher != null &&
                !existingPublisher.getId().equals(id)) {

            throw new PublisherAlreadyExistsException(
                    "Publisher already exists."
            );
        }

        publisher.setPublisherName(request.getPublisherName());
        publisher.setPublisherAddress(request.getPublisherAddress());
        publisher.setPublisherContact(request.getPublisherContact());

        Publisher updatedPublisher =
                publisherRepository.save(publisher);

        return publisherMapper.toResponse(updatedPublisher);
    }

    public PublisherResponse changePublisherStatus(
            UUID id,
            PublisherStatusRequest request
    ) {

        Publisher publisher = publisherRepository.findById(id)
                .orElseThrow(() ->
                        new PublisherNotFoundException(
                                "Publisher not found."
                        )
                );

        publisher.setStatus(request.getStatus());

        publisherRepository.save(publisher);

        return publisherMapper.toResponse(publisher);
    }
}
