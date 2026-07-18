package com.bookcorner.author.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorRequest {

    @NotBlank(message = "Author name is required.")
    @Size(max = 100, message = "Author name must not exceed 100 characters.")
    private String authorName;

    @Size(max = 500, message = "Author bio must not exceed 500 characters.")
    private String authorBio;

}
