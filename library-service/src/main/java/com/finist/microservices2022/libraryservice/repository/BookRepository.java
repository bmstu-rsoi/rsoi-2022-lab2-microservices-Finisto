package com.finist.microservices2022.libraryservice.repository;

import com.finist.microservices2022.libraryservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, Integer> {

    Book findBookByBookUid(UUID bookUid);
}
