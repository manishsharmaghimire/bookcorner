package com.bookcorner.author.dto;

import com.bookcorner.author.enums.AuthorStatus;
import lombok.*;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class AuthorResponse {

    private Long id;
    private String authorName;
    private String authorBio;
    private AuthorStatus status;
}
