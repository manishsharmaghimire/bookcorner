package com.bookcorner.publisher.dto;

import com.bookcorner.publisher.enums.PublisherStatus;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PublisherResponse {

    private UUID id;
    private String publisherName;
    private String publisherAddress;
    private String publisherContact;
    private PublisherStatus status;
}
