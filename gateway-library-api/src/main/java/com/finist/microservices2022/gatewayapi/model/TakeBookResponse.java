package com.finist.microservices2022.gatewayapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Date;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TakeBookResponse {

    String reservationUid;

    String status;

    String startDate;

    String tillDate;

    BookInfo book;

    LibraryResponse library;

    UserRatingResponse rating;

}
