package com.ivanalimin.spring_mvc_library.controller;

import com.ivanalimin.spring_mvc_library.model.Book;
import com.ivanalimin.spring_mvc_library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(value = BookController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class BookController {

    static final String REST_URL = "/rest/books";

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<Book>> getAll(Pageable pageable) {
        Page<Book> books = service.findAll(pageable);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable UUID id) {
        Book book = service.findById(id);
        return ResponseEntity.ok(book);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> create(@Valid @RequestBody Book book) {
        Book createdBook = service.save(book);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdBook.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdBook);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> update(@PathVariable UUID id, @Valid @RequestBody Book book) {
        Book updatedBook = service.update(id, book);
        return ResponseEntity.ok(updatedBook);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
