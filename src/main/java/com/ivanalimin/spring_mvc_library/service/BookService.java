package com.ivanalimin.spring_mvc_library.service;

import com.ivanalimin.spring_mvc_library.exception_handling.NotFoundException;
import com.ivanalimin.spring_mvc_library.model.Book;
import com.ivanalimin.spring_mvc_library.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class BookService {

    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Page<Book> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Book findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id:" + id));
    }

    @Transactional
    public Book save(Book book) {
        validateBook(book);
        return repository.save(book);
    }

    @Transactional
    public Book update(UUID id, Book book) {
        validateBook(book);
        return repository.findById(id)
                .map(bookToUpdate -> {
                    bookToUpdate.setTitle(book.getTitle());
                    bookToUpdate.setIsbn(book.getIsbn());
                    bookToUpdate.setPublicationDate(book.getPublicationDate());
                    bookToUpdate.setAuthors(book.getAuthors());
                    return repository.save(bookToUpdate);
                })
                .orElseThrow(() -> new NotFoundException("Book not found with id:" + id));
    }

    @Transactional
    public void delete(UUID id) {
        Book book = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Book not found with id:" + id));
        repository.delete(book);
    }

    private void validateBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }
        if (book.getTitle() == null || book.getTitle().isEmpty()) {
            throw new IllegalArgumentException("Book title is mandatory");
        }
        if (book.getIsbn() == null || book.getIsbn().isEmpty()) {
            throw new IllegalArgumentException("Book ISBN is mandatory");
        }
        if (book.getPublicationDate() == null) {
            throw new IllegalArgumentException("Published date is mandatory");
        }
    }
}
