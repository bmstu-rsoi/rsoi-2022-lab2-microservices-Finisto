package com.finist.microservices2022.libraryservice.controller;

import com.finist.microservices2022.gatewayapi.model.BookInfo;
import com.finist.microservices2022.gatewayapi.model.EditAvailableCountRequest;
import com.finist.microservices2022.gatewayapi.model.LibraryBookResponse;
import com.finist.microservices2022.gatewayapi.model.LibraryResponse;
import com.finist.microservices2022.libraryservice.model.Book;
import com.finist.microservices2022.libraryservice.model.Library;
import com.finist.microservices2022.libraryservice.model.LibraryBook;
import com.finist.microservices2022.libraryservice.repository.BookRepository;
import com.finist.microservices2022.libraryservice.repository.LibraryBookRepository;
import com.finist.microservices2022.libraryservice.repository.LibraryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1")
public class LibraryController {

    private final LibraryRepository libraryRepository;
    private final LibraryBookRepository libraryBookRepository;
    private final BookRepository bookRepository;

    public LibraryController (LibraryRepository libraryRepository,
                              LibraryBookRepository libraryBookRepository,
                              BookRepository bookRepository) {

        this.libraryRepository = libraryRepository;
        this.libraryBookRepository = libraryBookRepository;
        this.bookRepository = bookRepository;

    }

    @GetMapping(value = "/libraries")
    public ResponseEntity<List<LibraryResponse>> getLibrariesInCity(@RequestParam String city) {

        String decoded = URLDecoder.decode(city, StandardCharsets.UTF_8);
        List<Library> libraries = libraryRepository.findAllByCity(decoded);
        List<LibraryResponse> libraryResponses = new ArrayList<>();
        for (Library lib : libraries) {
            libraryResponses.add(new LibraryResponse(lib.getLibraryUid(),
                    lib.getName(), lib.getAddress(), lib.getCity()));
        }

        return new ResponseEntity<>(libraryResponses, HttpStatus.OK);
    }

    @GetMapping("/books")
    public ResponseEntity<List<LibraryBookResponse>> getBooksInLibrary(@RequestParam String libUid){
        List<LibraryBook> libraryBooks = libraryBookRepository.findLibraryBooksByLibraryId_LibraryUid(UUID.fromString(libUid));
        List<LibraryBookResponse> libraryBookResponses = new ArrayList<>();
        for (LibraryBook libBook : libraryBooks) {
            libraryBookResponses.add(new LibraryBookResponse(
                    libBook.getBookId().getBookUid().toString(),
                    libBook.getBookId().getName(),
                    libBook.getBookId().getAuthor(),
                    libBook.getBookId().getGenre(),
                    libBook.getBookId().getCondition(),
                    libBook.getAvailableCount()
                    ));
        }

        return new ResponseEntity<>(libraryBookResponses, HttpStatus.OK);
    }


    @PostMapping("/editAvailableCount")
    public ResponseEntity<Integer> editAvailableCount(@RequestBody EditAvailableCountRequest requestBody){
        String bookUid = requestBody.getBookUid();
        Integer byCount = requestBody.getByCount();
        Book book = bookRepository.findBookByBookUid(UUID.fromString(bookUid));
        LibraryBook lb = libraryBookRepository.findLibraryBookByBookId(book);
        Integer newCount = lb.getAvailableCount() + byCount;
        lb.setAvailableCount(newCount);
        libraryBookRepository.save(lb);

        return new ResponseEntity<Integer>(newCount, HttpStatus.OK);
    }

    @PostMapping("/book/editCondition")
    public ResponseEntity<String> editBookCondition(@RequestParam UUID bookUid, @RequestParam String condition){
        Book book = bookRepository.findBookByBookUid(bookUid);
        book.setCondition(condition);
        bookRepository.save(book);
        return new ResponseEntity<>(book.getCondition(), HttpStatus.OK);
    }


    @GetMapping("/book")
    public ResponseEntity<BookInfo> getBook(@RequestParam String bookUid){
        Book book = bookRepository.findBookByBookUid(UUID.fromString(bookUid));
        BookInfo bookInfo = new BookInfo(book.getBookUid().toString(), book.getName(), book.getAuthor(), book.getGenre());

        return new ResponseEntity<>(bookInfo, HttpStatus.OK);
    }

    @GetMapping("/library")
    public ResponseEntity<LibraryResponse> getLibrary(@RequestParam String libraryUid){
        Library library = libraryRepository.getLibraryByLibraryUid(UUID.fromString(libraryUid));
        LibraryResponse libraryResponse = new LibraryResponse(library.getLibraryUid(), library.getName(),
                library.getAddress(), library.getCity());

        return new ResponseEntity<>(libraryResponse, HttpStatus.OK);

    }

    @GetMapping("/libraryBook")
    public ResponseEntity<LibraryBookResponse> getLibraryBook(@RequestParam UUID bookUid){
        Book book = bookRepository.findBookByBookUid(bookUid);
        LibraryBook libraryBook = libraryBookRepository.findLibraryBookByBookId(book);
        LibraryBookResponse lbr = new LibraryBookResponse(
                book.getBookUid().toString(),
                book.getName(),
                book.getAuthor(),
                book.getGenre(),
                book.getCondition(),
                libraryBook.getAvailableCount()
        );
        return new ResponseEntity<>(lbr, HttpStatus.OK);
    }


    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

}
