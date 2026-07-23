package com.bookcorner.books.service.serviceimpl;


import com.bookcorner.books.dto.BookDetailsResponse;
import com.bookcorner.books.dto.BookListResponse;
import com.bookcorner.books.dto.CategoryRequest;
import com.bookcorner.books.dto.CategoryResponse;
import com.bookcorner.books.dto.CategoryStatusRequest;
import com.bookcorner.books.entity.Category;
import com.bookcorner.books.enums.CategoryStatus;
import com.bookcorner.books.exception.BookNotFoundException;
import com.bookcorner.books.exception.CategoryAlreadyExistsException;
import com.bookcorner.books.exception.CategoryNotFoundException;
import com.bookcorner.books.mapper.BookMapper;
import com.bookcorner.books.projection.BookDetailProjection;
import com.bookcorner.books.projection.BookProjection;
import com.bookcorner.books.repository.BookRepository;
import com.bookcorner.shared.dto.PageResponse;
import com.bookcorner.shared.dto.PaginationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {



    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    public PageResponse<BookListResponse> getAllActiveBooks(PaginationRequest request) {

        String sortBy = request.getSortBy() == null
                ? "title"
                : request.getSortBy();


        Sort.Direction direction = request.getDirection() == null ?
                Sort.Direction.ASC : request.getDirection();


        Sort sort = Sort.by(direction, sortBy);

        PageRequest pageRequest = PageRequest.of(request.getPage(), request.getSize(), sort);

        Page<BookProjection> bookPage = bookRepository.findAllActiveBooks(pageRequest);
        Page<BookListResponse> response = bookPage.map(bookMapper::toBookListResponse);
        return new PageResponse<>(
                response.getContent(),
                response.getNumber(),
                response.getSize(),
                response.getTotalElements(),
                response.getTotalPages(),
                response.isFirst(),
                response.isLast(),
                response.hasNext(),
                response.hasPrevious()
        );




    }
    public BookDetailsResponse getBookById(UUID id) {

        BookDetailProjection book = bookRepository
                .findActiveBookById(id)
                .orElseThrow(
                        () -> new BookNotFoundException(
                                "Book not found."
                        )
                );

        return bookMapper.toBookDetailsResponse(book);
    }

}
