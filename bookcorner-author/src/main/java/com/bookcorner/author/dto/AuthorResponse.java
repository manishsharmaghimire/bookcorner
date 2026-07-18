package com.bookcorner.author.dto;

import com.bookcorner.author.enums.AuthorStatus;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthorResponse {

    private UUID id;
    private String authorName;
    private String authorBio;
    private AuthorStatus status;
}
