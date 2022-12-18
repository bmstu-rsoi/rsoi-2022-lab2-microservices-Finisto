package com.finist.microservices2022.gatewaylibraryservice.controller;

import com.finist.microservices2022.gatewayapi.model.*;
import com.finist.microservices2022.gatewaylibraryservice.handler.RestTemplateResponseErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping("/api/v1")
public class GatewayController {

    @Value("${services.library-url}")
    private String library_url;

    @Value("${services.rating-url}")
    private String rating_url;

    @Value("${services.reservation-url}")
    private String reservation_url;


    private final RestTemplate restTemplate;

    public GatewayController(RestTemplateBuilder restTemplateBuilder) {
        restTemplate = restTemplateBuilder
//                .errorHandler(new RestTemplateResponseErrorHandler())
                .build();
    }

    @GetMapping(value = "/libraries")
    public ResponseEntity<LibraryPaginationResponse> getLibrariesInCity(@RequestParam String city,
                                                                        @RequestParam Integer page,
                                                                        @RequestParam Integer size)
            throws UnsupportedEncodingException {
        URI libUri = UriComponentsBuilder.fromHttpUrl(library_url)
                .path("/api/v1/libraries")
                .queryParam("city", URLEncoder.encode(city, StandardCharsets.UTF_8))
                .build()
//                .encode()
                .toUri();

        ResponseEntity<LibraryResponse[]> respEntity = this.restTemplate.getForEntity(libUri, LibraryResponse[].class);
        if (respEntity.getStatusCode() == HttpStatus.OK) {
            List<LibraryResponse> libList = List.of(Objects.requireNonNull(respEntity.getBody()));
            int totalElems = libList.size();
            List<LibraryResponse> pageLibList = new ArrayList<>();
            if (totalElems == 0) {
                return new ResponseEntity<>(new LibraryPaginationResponse(page, size, totalElems, pageLibList), HttpStatus.OK);
            }
            int pageCount = Math.ceilDiv(totalElems, size);
            if (pageCount == page) {
                pageLibList = libList.subList((page - 1) * size, totalElems);
            } else {
                pageLibList = libList.subList((page - 1) * size, page * size);
            }
            return new ResponseEntity<>(new LibraryPaginationResponse(page, size, totalElems, pageLibList), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(respEntity.getStatusCode());
        }
    }

    @GetMapping("/libraries/{libraryUid}/books")
    public ResponseEntity<LibraryBookPaginationResponse> getBooksInLibrary(@PathVariable String libraryUid, @RequestParam int page,
                                                                           @RequestParam int size, @RequestParam boolean showAll) {
        URI libUri = UriComponentsBuilder.fromHttpUrl(library_url)
                .path("/api/v1/books")
                .queryParam("libUid", libraryUid)
                .build()
//                .encode()
                .toUri();

        ResponseEntity<LibraryBookResponse[]> respEntity = this.restTemplate.getForEntity(libUri, LibraryBookResponse[].class);
        if (respEntity.getStatusCode() == HttpStatus.OK) {
            List<LibraryBookResponse> libList = new ArrayList<>(List.of(Objects.requireNonNull(respEntity.getBody())));

            if(!showAll){ // don't show books where availableCount == 0
                libList.removeIf(lbr -> (lbr.availableCount == 0));
            }

            int totalElems = libList.size();
            List<LibraryBookResponse> pageLibList = new ArrayList<>();
            if (totalElems == 0) {
                return new ResponseEntity<>(new LibraryBookPaginationResponse(page, size, totalElems, pageLibList), HttpStatus.OK);
            }
            int pageCount = Math.ceilDiv(totalElems, size);
            if (pageCount == page) {
                pageLibList = libList.subList((page - 1) * size, totalElems);
            } else {
                pageLibList = libList.subList((page - 1) * size, page * size);
            }
            return new ResponseEntity<>(new LibraryBookPaginationResponse(page, size, totalElems, pageLibList), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(respEntity.getStatusCode());
        }

    }


    @GetMapping("/rating")
    public ResponseEntity<?> getUserRating(@RequestHeader(name = "X-User-Name") String userName) {
        URI ratingUri = UriComponentsBuilder.fromHttpUrl(rating_url)
                .path("/api/v1/rating")
                .queryParam("username", userName)
                .build()
//                .encode()
                .toUri();

        ResponseEntity<?> respEntity = null;
        try {
            respEntity = this.restTemplate.getForEntity(ratingUri, UserRatingResponse.class);
            if (respEntity.getStatusCode() == HttpStatus.OK) {
                return new ResponseEntity<UserRatingResponse>((UserRatingResponse) respEntity.getBody(), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(respEntity.getStatusCode());
            }
        } catch (Exception ex) {
            return new ResponseEntity<ErrorResponse>(new ErrorResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
        }

    }

}
