package com.finist.microservices2022.libraryservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "books")
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Integer id;

    @Column(name = "book_uid")
    @Getter @Setter
    private UUID bookUid;

    @Column(name = "`name`")
    @Getter @Setter
    private String name;

    @Column(name = "author")
    @Getter @Setter
    private String author;

    @Column(name = "genre")
    @Getter @Setter
    private String genre;

    @Column(name = "condition")
    @Getter @Setter
    private String condition;

//    @OneToOne
//    @JoinColumn(name = "library_id", nullable = false)
//    @Getter @Setter
//    private Library libraryId;


}
