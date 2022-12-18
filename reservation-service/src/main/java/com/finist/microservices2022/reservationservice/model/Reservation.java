package com.finist.microservices2022.reservationservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Integer id;

    @Column(name = "reservation_uid")
    @Getter @Setter
    private UUID reservationUid;

    @Column(name = "username")
    @Getter @Setter
    private String username;

    @Column(name = "book_uid")
    @Getter @Setter
    private UUID bookUid;

    @Column(name = "library_uid")
    @Getter @Setter
    private UUID libraryUid;

    @Column(name = "status")
    @Getter @Setter
    private String status;

    @Column(name = "start_date")
    @Getter @Setter
    private Date startDate;

    @Column(name = "till_date")
    @Getter @Setter
    private Date tillDate;
}
