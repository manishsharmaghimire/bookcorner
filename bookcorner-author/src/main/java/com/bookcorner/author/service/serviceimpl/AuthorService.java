package com.bookcorner.author.service.serviceimpl;


import com.bookcorner.author.dto.AuthorRequest;
import com.bookcorner.author.dto.AuthorResponse;
import com.bookcorner.author.dto.AuthorStatusRequest;
import com.bookcorner.author.entity.Author;
import com.bookcorner.author.enums.AuthorStatus;
import com.bookcorner.author.exception.AuthorAlreadyExistsException;
import com.bookcorner.author.exception.AuthorNotFoundException;
import com.bookcorner.author.mapper.AuthorMapper;
import com.bookcorner.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorService {

   private final AuthorRepository authorRepository;
   private final AuthorMapper authorMapper;


   public AuthorResponse createAuthor(AuthorRequest authorRequest) {

       if(authorRepository.existByAuthorName(authorRequest.getAuthorName())) {
           throw new AuthorAlreadyExistsException();
       }


       Author author = new Author();
       author.setAuthorName(authorRequest.getAuthorName());
       author.setAuthorBio(authorRequest.getAuthorBio());
       author.setStatus(AuthorStatus.ACTIVE);
       Author save = authorRepository.save(author);
       return authorMapper.toResponse(save);

   }
    public AuthorResponse getAuthorById(UUID id) {

        Author author = authorRepository.findById(id)
                .orElseThrow(
                        () -> new AuthorNotFoundException(
                                "Author not found."
                        )
                );

        return authorMapper.toResponse(author);
    }

    public List<AuthorResponse> getAllActiveAuthors() {

        List<Author> authors = authorRepository.findByStatus(
                AuthorStatus.ACTIVE
        );

        return authors.stream()
                .map(authorMapper::toResponse)
                .toList();
    }


    public AuthorResponse updateAuthor(
            UUID id,
            AuthorRequest request
    ) {

        Author author = authorRepository.findById(id)
                .orElseThrow(
                        () -> new AuthorNotFoundException(
                                "Author not found."
                        )
                );

        Author existingAuthor = authorRepository
                .findByAuthorName(request.getAuthorName())
                .orElse(null);

        if (existingAuthor != null &&
                !existingAuthor.getId().equals(id)) {

            throw new AuthorAlreadyExistsException(
                    "Author already exists."
            );
        }

        author.setAuthorName(request.getAuthorName());
        author.setAuthorBio(request.getAuthorBio());

        Author updatedAuthor =
                authorRepository.save(author);

        return authorMapper.toResponse(updatedAuthor);
    }

    public AuthorResponse changeAuthorStatus(
            UUID id,
            AuthorStatusRequest request
    ) {

        Author author = authorRepository.findById(id)
                .orElseThrow(() ->
                        new AuthorNotFoundException(
                                "Author not found."
                        )
                );

        author.setStatus(request.getStatus());

        authorRepository.save(author);

        return authorMapper.toResponse(author);
    }
}
