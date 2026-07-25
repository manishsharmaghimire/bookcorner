package com.bookcorner.publisher.dto;

import com.bookcorner.publisher.enums.PublisherStatus;
import lombok.*;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class PublisherResponse {

    private Long id;
    private String publisherName;
    private String publisherAddress;
    private String publisherContact;
    private PublisherStatus status;
}
