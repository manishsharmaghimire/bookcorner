package com.bookcorner.publisher.dto;

import com.bookcorner.publisher.enums.PublisherStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublisherStatusRequest {

    @NotNull(message = "Publisher status is required.")
    private PublisherStatus status;

}
