package com.bookcorner.author.dto;

import com.bookcorner.author.enums.AuthorStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorStatusRequest {

    @NotNull(message = "Author status is required.")
    private AuthorStatus status;

}
