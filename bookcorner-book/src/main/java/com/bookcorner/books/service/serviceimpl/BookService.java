package com.bookcorner.books.service.serviceimpl;


import com.bookcorner.books.dto.BookListResponse;
import com.bookcorner.books.dto.CategoryRequest;
import com.bookcorner.books.dto.CategoryResponse;
import com.bookcorner.books.dto.CategoryStatusRequest;
import com.bookcorner.books.entity.Category;
import com.bookcorner.books.enums.CategoryStatus;
import com.bookcorner.books.exception.CategoryAlreadyExistsException;
import com.bookcorner.books.exception.CategoryNotFoundException;
import com.bookcorner.books.mapper.BookMapper;
import com.bookcorner.books.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {



    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public Page<BookListResponse> getAllActiveBooks(Pageable pageable) {






    }

}
