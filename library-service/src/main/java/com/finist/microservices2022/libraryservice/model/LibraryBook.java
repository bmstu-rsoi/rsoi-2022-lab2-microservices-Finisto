package com.finist.microservices2022.libraryservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

//@Entity fixme add id field or not
@Table(name = "library_books")
@NoArgsConstructor
@AllArgsConstructor
public class LibraryBook {

    @OneToOne()
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Book bookId;

    @OneToOne
    @Column(name = "library_id")
    @Getter @Setter
    private Library libraryId;

    @Column(name = "available_count")
    @Getter @Setter
    private Integer availableCount;


}
