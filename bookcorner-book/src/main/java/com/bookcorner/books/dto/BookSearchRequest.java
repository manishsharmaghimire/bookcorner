package com.bookcorner.books.dto;

import com.bookcorner.shared.dto.PaginationRequest;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchRequest extends PaginationRequest {

    private String keyword;
}
