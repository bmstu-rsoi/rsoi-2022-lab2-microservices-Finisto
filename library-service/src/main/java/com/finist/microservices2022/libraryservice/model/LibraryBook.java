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

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Integer bookId;

    @Column(name = "library_id")
    @Getter @Setter
    private Integer libraryId;

    @Column(name = "available_count")
    @Getter @Setter
    private Integer availableCount;


}
