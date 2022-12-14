package com.finist.microservices2022.libraryservice.controller;

import com.finist.microservices2022.gatewayapi.model.LibraryBookResponse;
import com.finist.microservices2022.gatewayapi.model.LibraryResponse;
import com.finist.microservices2022.libraryservice.model.Library;
import com.finist.microservices2022.libraryservice.model.LibraryBook;
import com.finist.microservices2022.libraryservice.repository.LibraryBookRepository;
import com.finist.microservices2022.libraryservice.repository.LibraryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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

    public LibraryController (LibraryRepository libraryRepository, LibraryBookRepository libraryBookRepository) {

        this.libraryRepository = libraryRepository;
        this.libraryBookRepository = libraryBookRepository;
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

    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

}
