package com.finist.microservices2022.reservationservice.controller;

import com.finist.microservices2022.gatewayapi.model.TakeBookRequest;
import com.finist.microservices2022.gatewayapi.model.UserReservationResponse;
import com.finist.microservices2022.reservationservice.model.Reservation;
import com.finist.microservices2022.reservationservice.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class ReservationController {
    private final ReservationRepository reservationRepository;


    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    @GetMapping("/reservations")
    public ResponseEntity<List<UserReservationResponse>> getUserReservations(@RequestParam String username) {
        List<Reservation> reservationList = reservationRepository.getReservationsByUsername(username);
        List<UserReservationResponse> responses = new ArrayList<>();
        for (Reservation res : reservationList) {
            responses.add(new UserReservationResponse(
                    res.getReservationUid().toString(),
                    res.getBookUid().toString(),
                    res.getLibraryUid().toString(),
                    res.getStatus(),
                    res.getStartDate(),
                    res.getTillDate()
            ));
        }

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }


    @PostMapping("/reservation")
    public ResponseEntity<UserReservationResponse> createReservation(@RequestParam String username, @RequestBody TakeBookRequest requestBody) {

//        Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        Instant tillDate = Instant.parse(requestBody.getTillDate());
//        String tillDateStringInFormat = formatter.format(tillDate);
//        Timestamp ts = Timestamp.valueOf(tillDateStringInFormat);

        UUID reservationUid = UUID.randomUUID();
        Reservation newReservation = new Reservation(null, reservationUid, username,
                UUID.fromString(requestBody.getBookUid()),
                UUID.fromString(requestBody.getLibraryUid()),
                "RENTED",
                Date.from(Instant.now()),
                requestBody.getTillDate()
        );

        newReservation = reservationRepository.save(newReservation);

        return new ResponseEntity<>(new UserReservationResponse(
                newReservation.getReservationUid().toString(),
                newReservation.getBookUid().toString(),
                newReservation.getLibraryUid().toString(),
                newReservation.getStatus(),
                newReservation.getStartDate(),
                newReservation.getTillDate()
        ), HttpStatus.OK);
    }


    @GetMapping("/reservation")
    public ResponseEntity<UserReservationResponse> getUserReservation(@RequestParam UUID reservationUid){
        Reservation reservation = reservationRepository.getReservationByReservationUid(reservationUid);
        UserReservationResponse urr = new UserReservationResponse(
                reservation.getReservationUid().toString(),
                reservation.getBookUid().toString(),
                reservation.getLibraryUid().toString(),
                reservation.getStatus(),
                reservation.getStartDate(),
                reservation.getTillDate()
        );
        return new ResponseEntity<>(urr, HttpStatus.OK);

    }

    @PostMapping("/changeStatus")
    public ResponseEntity<?> changeReservationStatus(@RequestParam UUID reservationUid, @RequestParam String status){
        Reservation reservation = reservationRepository.getReservationByReservationUid(reservationUid);
        reservation.setStatus(status);
        reservationRepository.save(reservation);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
