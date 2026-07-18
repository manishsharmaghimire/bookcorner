package com.bookcorner.books.dto;


import com.bookcorner.books.enums.BookStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookStatusRequest {

    @NotNull(message = "Status is required.")
    private BookStatus status;

}