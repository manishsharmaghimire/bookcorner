package com.bookcorner.books.service.serviceimpl;


import com.bookcorner.author.entity.Author;
import com.bookcorner.author.exception.AuthorNotFoundException;
import com.bookcorner.author.repository.AuthorRepository;
import com.bookcorner.books.dto.*;
import com.bookcorner.books.entity.Books;
import com.bookcorner.books.entity.Category;
import com.bookcorner.books.enums.BookStatus;
import com.bookcorner.books.exception.BookAlreadyExistsException;
import com.bookcorner.books.exception.BookNotFoundException;
import com.bookcorner.books.exception.CategoryNotFoundException;
import com.bookcorner.books.mapper.BookMapper;
import com.bookcorner.books.projection.BookDetailProjection;
import com.bookcorner.books.projection.BookProjection;
import com.bookcorner.books.repository.BookRepository;
import com.bookcorner.books.repository.CategoryRepository;
import com.bookcorner.publisher.entity.Publisher;
import com.bookcorner.publisher.exception.PublisherNotFoundException;
import com.bookcorner.publisher.repository.PublisherRepository;
import com.bookcorner.shared.dto.PageResponse;
import com.bookcorner.shared.dto.PaginationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookService {


    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final CategoryRepository categoryRepository;
    private final AuthorRepository authorRepository;
    private final PublisherRepository publisherRepository;


    public BookResponse createBook(BookRequest request) {


        boolean isbnCheck = bookRepository.existsByIsbn(request.getIsbn());

        if (isbnCheck) {
            throw new BookAlreadyExistsException("Book already exists.");

        }

        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(CategoryNotFoundException::new);

        Author author = authorRepository.findById(
                request.getAuthorId()
        ).orElseThrow(
                () -> new AuthorNotFoundException(
                        "Author not found."
                )
        );
        Publisher publisher = publisherRepository.findById(
                request.getPublisherId()
        ).orElseThrow(
                () -> new PublisherNotFoundException(
                        "Publisher not found."
                )
        );

        Books books = Books.builder()

                .title(request.getTitle())
                .isbn(request.getIsbn())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .language(request.getLanguage())
                .edition(request.getEdition())
                .publicationDate(request.getPublicationDate())
                .coverImageUrl(request.getCoverImageUrl())
                .pages(request.getPages())
                .status(BookStatus.ACTIVE)
                .category(category)
                .author(author)
                .publisher(publisher)

                .build();

        Books savedBook = bookRepository.save(books);
        return bookMapper.toBookResponse(savedBook);

    }


    public BookResponse updateBooks(UUID id, BookRequest request){

        Books book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException("Book not found."));
        if (!book.getIsbn().equals(request.getIsbn())
                && bookRepository.existsByIsbn(request.getIsbn())) {

            throw new BookAlreadyExistsException(
                    "Book with this ISBN already exists."
            );
        }


        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(CategoryNotFoundException::new);
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(
                        () -> new AuthorNotFoundException(
                                "Author not found."
                        )
                );

        Publisher publisher = publisherRepository.findById(request.getPublisherId())
                .orElseThrow(
                        () -> new PublisherNotFoundException(
                                "Publisher not found."
                        )
                );
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setDescription(request.getDescription());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock());
        book.setLanguage(request.getLanguage());
        book.setEdition(request.getEdition());
        book.setPublicationDate(request.getPublicationDate());
        book.setCoverImageUrl(request.getCoverImageUrl());
        book.setPages(request.getPages());

        book.setCategory(category);
        book.setAuthor(author);
        book.setPublisher(publisher);

        Books updatedBook = bookRepository.save(book);

        return bookMapper.toBookResponse(updatedBook);
    }





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

    public void deleteBook(UUID id) {

        Books book = bookRepository.findById(id)
                .orElseThrow(
                        () -> new BookNotFoundException(
                                "Book not found."
                        )
                );

        book.setStatus(BookStatus.DISCONTINUED);

        bookRepository.save(book);
    }
}
