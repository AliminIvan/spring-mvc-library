package com.ivanalimin.spring_mvc_library.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ivanalimin.spring_mvc_library.model.Book;
import com.ivanalimin.spring_mvc_library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
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
    void getAll_ShouldReturnBooksPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> page = new PageImpl<>(List.of(sampleBook));
        when(service.findAll(pageable)).thenReturn(page);

        mockMvc.perform(get("/rest/books")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value(sampleBook.getTitle()));

        verify(service, times(1)).findAll(pageable);
    }

    @Test
    void getById_ShouldReturnBook_WhenBookExists() throws Exception {
        UUID bookId = sampleBook.getId();
        when(service.findById(bookId)).thenReturn(sampleBook);

        mockMvc.perform(get("/rest/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(sampleBook.getTitle()));

        verify(service, times(1)).findById(bookId);
    }

    @Test
    void create_ShouldReturnCreatedBook() throws Exception {
        when(service.save(any(Book.class))).thenReturn(sampleBook);

        mockMvc.perform(post("/rest/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleBook)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(sampleBook.getTitle()));

        verify(service, times(1)).save(any(Book.class));
    }

    @Test
    void update_ShouldReturnUpdatedBook() throws Exception {
        UUID bookId = sampleBook.getId();
        Book updatedBook = new Book();
        updatedBook.setId(bookId);
        updatedBook.setTitle("Updated Book");
        updatedBook.setIsbn("123-456-789");
        updatedBook.setPublicationDate(LocalDate.of(1990, 9, 22));
        updatedBook.setAuthors(sampleBook.getAuthors());

        when(service.update(any(UUID.class), any(Book.class))).thenReturn(updatedBook);

        mockMvc.perform(put("/rest/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBook)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.isbn").value("123-456-789"))
                .andExpect(jsonPath("$.publicationDate").value("1990-09-22"));

        verify(service, times(1)).update(any(UUID.class), any(Book.class));
    }

    @Test
    void delete_ShouldReturnNoContent_WhenBookIsDeleted() throws Exception {
        UUID bookId = sampleBook.getId();
        doNothing().when(service).delete(bookId);

        mockMvc.perform(delete("/rest/books/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(service, times(1)).delete(bookId);
    }
}
