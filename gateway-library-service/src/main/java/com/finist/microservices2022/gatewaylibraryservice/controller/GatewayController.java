package com.finist.microservices2022.gatewaylibraryservice.controller;

import com.finist.microservices2022.gatewayapi.model.LibraryPaginationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class GatewayController {

    @GetMapping("/libraries")
    public ResponseEntity<LibraryPaginationResponse> getLibrariesInCity(String city, Integer page, Integer size){

        //todo
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

}
