package com.finist.microservices2022.libraryservice.repository;

import com.finist.microservices2022.libraryservice.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Integer> {

}
