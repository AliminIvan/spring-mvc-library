package com.ivanalimin.spring_mvc_library.repository;

import com.ivanalimin.spring_mvc_library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(readOnly = true)
public interface BookRepository extends JpaRepository<Book, UUID> {
}
