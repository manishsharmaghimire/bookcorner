package com.bookcorner.author.mapper;

import com.bookcorner.author.dto.AuthorResponse;
import com.bookcorner.author.entity.Author;
import org.springframework.stereotype.Component;

@Component
public class AuthorMapper {

    public AuthorResponse toResponse(Author author) {
        return new AuthorResponse(
                author.getId(),
                author.getAuthorName(),
                author.getAuthorBio(),
                author.getStatus()
        );
    }
}
