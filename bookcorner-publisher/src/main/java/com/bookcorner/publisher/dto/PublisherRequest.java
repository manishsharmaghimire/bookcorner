package com.bookcorner.publisher.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherRequest {

    @NotBlank(message = "Publisher name is required.")
    @Size(max = 100, message = "Publisher name must not exceed 100 characters.")
    private String publisherName;

    @Size(max = 500, message = "Publisher address must not exceed 500 characters.")
    private String publisherAddress;

    @Size(max = 20, message = "Publisher contact must not exceed 20 characters.")
    private String publisherContact;

}
