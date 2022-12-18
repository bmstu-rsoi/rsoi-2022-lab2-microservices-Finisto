package com.finist.microservices2022.gatewaylibraryservice.controller;

import com.finist.microservices2022.gatewayapi.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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

            if (!showAll) { // don't show books where availableCount == 0
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


    @PostMapping("/reservations")
    public ResponseEntity<?> takeBookFromLibrary(@RequestHeader(name = "X-User-Name") String userName, @RequestBody TakeBookRequest requestBody) {

        // get amount of already taken books by user
        List<UserReservationResponse> userReservations = getUserReservationsResponse(userName);

        // get rating of user
        UserRatingResponse urr = getUserRatingResponse(userName);

        // check if user can take new book
        boolean canTakeNewBook = urr.getStars() / 10 > userReservations.size();

        if (canTakeNewBook) {
            // created RENTED entry in Reservation system
            UserReservationResponse userReservationResponse = postUserReservationEntry(userName, requestBody);

            // decrease available count in Library system
            editAvailableCountByCountRequest(requestBody.getBookUid(), -1);

            // get book info
            BookInfo bookInfo = getBookInfo(requestBody.getBookUid());

            // get library info
            LibraryResponse libraryResponse = getLibraryResponse(requestBody.getLibraryUid());

            DateFormat outFormatter = new SimpleDateFormat("yyyy-MM-dd");
            return new ResponseEntity<>(new TakeBookResponse(
                    userReservationResponse.getReservationUid(),
                    userReservationResponse.getStatus(),
                    outFormatter.format(userReservationResponse.getStartDate()),
                    outFormatter.format(userReservationResponse.getTillDate()),
                    bookInfo,
                    libraryResponse,
                    urr
            ), HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new ErrorResponse("User %s have been rented maximum amount of books".formatted(userName)),
                    HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/reservations")
    public ResponseEntity<List<BookReservationResponse>> getAllReservations(@RequestHeader(name = "X-User-Name") String userName){
        URI reservationUri = UriComponentsBuilder.fromHttpUrl(reservation_url)
                .path("/api/v1/reservations")
                .queryParam("username", userName)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<UserReservationResponse[]> respEntity = null;
        respEntity = this.restTemplate.getForEntity(reservationUri, UserReservationResponse[].class);
        List<UserReservationResponse> reservationResponseList = new ArrayList<>(List.of(Objects.requireNonNull(respEntity.getBody())));

        List<BookReservationResponse> bookReservationResponseList = new ArrayList<>();
        DateFormat outFormatter = new SimpleDateFormat("yyyy-MM-dd");
        for(UserReservationResponse urr : reservationResponseList){
            BookInfo book = getBookInfo(urr.getBookUid());
            LibraryResponse library = getLibraryResponse(urr.getLibraryUid());

            bookReservationResponseList.add(new BookReservationResponse(
                    UUID.fromString(urr.getReservationUid()),
                    urr.getStatus(),
                    outFormatter.format(urr.getStartDate()),
                    outFormatter.format(urr.getTillDate()),
                    book,
                    library
            ));
        }

        return new ResponseEntity<>(bookReservationResponseList, HttpStatus.OK);
    }


    @PostMapping("/reservations/{reservationUid}/return")
    public ResponseEntity<?> returnBookToLibrary(@PathVariable UUID reservationUid,
                                                @RequestHeader(name = "X-User-Name") String userName,
                                                @RequestBody ReturnBookRequest requestBody){

        // check if reservation exist
        UserReservationResponse userReservationResponse = getUserReservationResponse(reservationUid);
        if(userReservationResponse == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // change reservation status
        String newStatus = (requestBody.getDate().after(userReservationResponse.getTillDate())) ? "EXPIRED" : "RETURNED";
        changeReservationStatusRequest(newStatus, reservationUid);

        // increase available count in library system
        editAvailableCountByCountRequest(userReservationResponse.getBookUid(), 1);

        // get old info of book
        LibraryBookResponse libraryBook = getLibraryBookResponseRequest(userReservationResponse.getBookUid());
        String oldCondition = libraryBook.getCondition();

        // update condition of book
        String newCondition = editBookConditionRequest(userReservationResponse.getBookUid(), requestBody.getCondition());

        Integer ratingOffset = 0;
        // check if expired
        if(newStatus == "EXPIRED")
            ratingOffset -= 10;

        // check if condition decreased and decrease rating for each condition
        switch (oldCondition) {
            case "EXCELLENT" -> ratingOffset -= 20;
            case "GOOD" -> ratingOffset -= 10;
        }
        switch (newCondition){
            case "EXCELLENT" -> ratingOffset += 20;
            case "GOOD" -> ratingOffset += 10;
        }

        // if not expired and condition has not decreased
        if(!newStatus.equals("EXPIRED") && ratingOffset >= 0){
            // then increase rating by 1 star
            ratingOffset += 1;
        }

        // edit user rating by ratingOffset
        Integer newRating = editUserRatingByOffset(userName, ratingOffset);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }



    private UserRatingResponse getUserRatingResponse(String userName) {
        URI ratingUri = UriComponentsBuilder.fromHttpUrl(rating_url)
                .path("/api/v1/rating")
                .queryParam("username", userName)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<UserRatingResponse> respEntity = null;
        respEntity = this.restTemplate.getForEntity(ratingUri, UserRatingResponse.class);
        return respEntity.getBody();
    }

    private Integer editUserRatingByOffset(String userName, Integer offset) {
        URI ratingUri = UriComponentsBuilder.fromHttpUrl(rating_url)
                .path("/api/v1/rating/edit")
                .queryParam("username", userName)
                .queryParam("offset", offset)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<Integer> respEntity = null;
        respEntity = this.restTemplate.postForEntity(ratingUri, null, Integer.class);
        return respEntity.getBody();
    }

    private List<UserReservationResponse> getUserReservationsResponse(String userName) {
        URI reservationUri = UriComponentsBuilder.fromHttpUrl(reservation_url)
                .path("/api/v1/reservations")
                .queryParam("username", userName)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<UserReservationResponse[]> respEntity = null;
        respEntity = this.restTemplate.getForEntity(reservationUri, UserReservationResponse[].class);
        return new ArrayList<>(List.of(Objects.requireNonNull(respEntity.getBody())));
    }

    private UserReservationResponse getUserReservationResponse(UUID reservationUid) {
        URI reservationUri = UriComponentsBuilder.fromHttpUrl(reservation_url)
                .path("/api/v1/reservation")
                .queryParam("reservationUid", reservationUid)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<UserReservationResponse> respEntity = null;
        respEntity = this.restTemplate.getForEntity(reservationUri, UserReservationResponse.class);
        return respEntity.getBody();
    }

    private UserReservationResponse postUserReservationEntry(String userName, TakeBookRequest request){
        URI reservationUri = UriComponentsBuilder.fromHttpUrl(reservation_url)
                .path("/api/v1/reservation")
                .queryParam("username", userName)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<UserReservationResponse> respEntity = null;
        respEntity = this.restTemplate.postForEntity(reservationUri, request, UserReservationResponse.class);

        return respEntity.getBody();
//        respEntity = this.restTemplate.getForEntity(reservationUri, UserReservationResponse[].class);
//        return new ArrayList<>(List.of(Objects.requireNonNull(respEntity.getBody())));
    }

    private UserReservationResponse changeReservationStatusRequest(String status, UUID reservationUid){
        URI reservationUri = UriComponentsBuilder.fromHttpUrl(reservation_url)
                .path("/api/v1/changeStatus")
                .queryParam("status", status)
                .queryParam("reservationUid", reservationUid)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<UserReservationResponse> respEntity = null;
        respEntity = this.restTemplate.postForEntity(reservationUri, null, UserReservationResponse.class);

        return respEntity.getBody();
    }

    private void editAvailableCountByCountRequest(String bookUid, Integer byCount){
        URI libraryUri = UriComponentsBuilder.fromHttpUrl(library_url)
                .path("/api/v1/editAvailableCount")
//                ...
//                .queryParam("bookUid", bookUid)
//                .queryParam("byCount", byCount)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<Integer> respEntity = null;
        HttpHeaders headers = new HttpHeaders();
        headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "merge-patch+json");
        headers.setContentType(mediaType);
        HttpEntity<EditAvailableCountRequest> httpEntity = new HttpEntity<>(new EditAvailableCountRequest(bookUid, byCount),headers);
        respEntity = this.restTemplate.exchange(libraryUri, HttpMethod.POST, httpEntity, Integer.class);

        return;
    }

    private String editBookConditionRequest(String bookUid, String condition){
        URI libraryUri = UriComponentsBuilder.fromHttpUrl(library_url)
                .path("/api/v1/book/editCondition")
                .queryParam("bookUid", bookUid)
                .queryParam("condition", condition)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<String> respEntity = null;
        respEntity = this.restTemplate.postForEntity(libraryUri, null, String.class);
        return respEntity.getBody();
    }


    private BookInfo getBookInfo(String bookUid){
        URI libraryUri = UriComponentsBuilder.fromHttpUrl(library_url)
                .path("/api/v1/book")
                .queryParam("bookUid", bookUid)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<BookInfo> respEntity = null;
        respEntity = this.restTemplate.getForEntity(libraryUri, BookInfo.class);

        return respEntity.getBody();
    }

    private LibraryResponse getLibraryResponse(String libraryUid){
        URI libraryUri = UriComponentsBuilder.fromHttpUrl(library_url)
                .path("/api/v1/library")
                .queryParam("libraryUid", libraryUid)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<LibraryResponse> respEntity = null;
        respEntity = this.restTemplate.getForEntity(libraryUri, LibraryResponse.class);

        return respEntity.getBody();
    }

    private LibraryBookResponse getLibraryBookResponseRequest(String bookUid){
        URI libraryUri = UriComponentsBuilder.fromHttpUrl(library_url)
                .path("/api/v1/libraryBook")
                .queryParam("bookUid", bookUid)
                .build()
//                .encode()
                .toUri();
        ResponseEntity<LibraryBookResponse> respEntity = null;
        respEntity = this.restTemplate.getForEntity(libraryUri, LibraryBookResponse.class);

        return respEntity.getBody();
    }

}
