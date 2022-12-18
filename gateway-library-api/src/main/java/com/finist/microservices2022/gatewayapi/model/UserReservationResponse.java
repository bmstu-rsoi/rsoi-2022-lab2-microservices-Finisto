package com.finist.microservices2022.gatewayapi.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserReservationResponse {

    String reservationUid;

    String bookUid;

    String libraryUid;

    String status;

    Date startDate;

    Date tillDate;

}
