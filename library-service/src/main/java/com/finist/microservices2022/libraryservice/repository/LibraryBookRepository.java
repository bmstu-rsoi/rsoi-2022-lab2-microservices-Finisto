package com.finist.microservices2022.libraryservice.repository;

import com.finist.microservices2022.libraryservice.model.Book;
import com.finist.microservices2022.libraryservice.model.LibraryBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LibraryBookRepository extends JpaRepository<LibraryBook, Integer> {

    List<LibraryBook> findLibraryBooksByLibraryId_LibraryUid(UUID libraryUid);

    LibraryBook findLibraryBookByBookId(Book bookId);

}
