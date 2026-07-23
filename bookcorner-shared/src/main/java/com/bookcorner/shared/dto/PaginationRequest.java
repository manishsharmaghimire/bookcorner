package com.bookcorner.shared.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@Setter
@Getter
public class PaginationRequest {

    private int page;

    private int size;

    private String sortBy;

    private Sort.Direction direction;

}
