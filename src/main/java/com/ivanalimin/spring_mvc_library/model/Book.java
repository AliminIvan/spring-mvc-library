package com.ivanalimin.spring_mvc_library.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @NotEmpty(message = "Book title is mandatory")
    @Column(name = "title", nullable = false)
    private String title;

    @NotEmpty(message = "Book ISBN is mandatory")
    @Column(name = "isbn", nullable = false, unique = true)
    private String isbn;

    @NotNull(message = "Published date is mandatory")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @JsonIgnore
    private Set<Author> authors;
}
