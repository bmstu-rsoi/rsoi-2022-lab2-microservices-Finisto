package com.finist.microservices2022.libraryservice.controller;

import com.finist.microservices2022.gatewayapi.model.LibraryResponse;
import com.finist.microservices2022.libraryservice.model.Library;
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

@RestController
@RequestMapping(value = "/api/v1")
public class LibraryController {

    private final LibraryRepository libraryRepository;

    public LibraryController (LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
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

    @Bean
    CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true);
        return filter;
    }

}
