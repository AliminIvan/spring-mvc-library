package com.ivanalimin.spring_mvc_library.service;

import com.ivanalimin.spring_mvc_library.exception_handling.NotFoundException;
import com.ivanalimin.spring_mvc_library.model.Book;
import com.ivanalimin.spring_mvc_library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository repository;

    @InjectMocks
    private BookService service;

    private Book sampleBook;

    @BeforeEach
    void setUp() {
        sampleBook = new Book();
        sampleBook.setId(UUID.randomUUID());
        sampleBook.setTitle("Sample Book");
        sampleBook.setIsbn("123-456-789");
        sampleBook.setPublicationDate(LocalDate.of(2024, 8, 26));
        sampleBook.setAuthors(new HashSet<>());
    }

    @Test
    void findAll_ShouldReturnBooksPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<>(List.of(sampleBook));
        when(repository.findAll(pageable)).thenReturn(page);

        Page<Book> result = service.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    void findById_ShouldReturnBook_WhenBookExists() {
        UUID bookId = sampleBook.getId();
        when(repository.findById(bookId)).thenReturn(Optional.of(sampleBook));

        Book result = service.findById(bookId);

        assertNotNull(result);
        assertEquals(sampleBook.getTitle(), result.getTitle());
        verify(repository, times(1)).findById(bookId);
    }

    @Test
    void findById_ShouldThrowNotFoundException_WhenBookDoesNotExist() {
        UUID bookId = sampleBook.getId();
        when(repository.findById(bookId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.findById(bookId));
        verify(repository, times(1)).findById(bookId);
    }

    @Test
    void save_ShouldSaveBook_WhenBookIsValid() {
        when(repository.save(any(Book.class))).thenReturn(sampleBook);

        Book result = service.save(sampleBook);

        assertNotNull(result);
        assertEquals(sampleBook.getTitle(), result.getTitle());
        verify(repository, times(1)).save(sampleBook);
    }

    @Test
    void update_ShouldUpdateBook_WhenBookIsValidAndExists() {
        UUID bookId = sampleBook.getId();
        when(repository.findById(bookId)).thenReturn(Optional.of(sampleBook));
        when(repository.save(any(Book.class))).thenReturn(sampleBook);

        Book result = service.update(bookId, sampleBook);

        assertNotNull(result);
        assertEquals(sampleBook.getTitle(), result.getTitle());
        verify(repository, times(1)).findById(bookId);
        verify(repository, times(1)).save(sampleBook);
    }

    @Test
    void delete_ShouldDeleteBook_WhenBookExists() {
        UUID bookId = sampleBook.getId();
        when(repository.findById(bookId)).thenReturn(Optional.of(sampleBook));

        service.delete(bookId);

        verify(repository, times(1)).delete(sampleBook);
    }
}
