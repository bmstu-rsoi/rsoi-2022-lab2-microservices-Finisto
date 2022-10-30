package com.finist.microservices2022.libraryservice.controller;

import com.finist.microservices2022.gatewayapi.model.LibraryResponse;
import com.finist.microservices2022.libraryservice.model.Library;
import com.finist.microservices2022.libraryservice.repository.LibraryRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LibraryController {

    private LibraryRepository libraryRepository;

    @GetMapping("/libraries")
    public ResponseEntity<List<LibraryResponse>> getLibrariesInCity(@RequestParam String city) {

        List<Library> libraries = libraryRepository.findAllByCity(city);
        List<LibraryResponse> libraryResponses = new ArrayList<>();
        for (Library lib : libraries) {
            libraryResponses.add(new LibraryResponse(lib.getLibraryUid(),
                    lib.getName(), lib.getAddress(), lib.getCity()));
        }

        return new ResponseEntity<>(libraryResponses, HttpStatus.OK);
    }

}
