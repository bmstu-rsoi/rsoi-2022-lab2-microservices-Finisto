package com.finist.microservices2022.libraryservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "library")
@NoArgsConstructor
@AllArgsConstructor
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Integer id;

    @Column(name = "library_uid")
    @Getter @Setter
    private UUID libraryUid;

    @Column(name = "`name`")
    @Getter @Setter
    private String name;

    @Column(name = "city")
    @Getter @Setter
    private String city;

    @Column(name = "address")
    @Getter @Setter
    private String address;


}
