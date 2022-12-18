package com.finist.microservices2022.reservationservice.repository;

import com.finist.microservices2022.reservationservice.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {

    List<Reservation> getReservationsByUsername(String username);


}
