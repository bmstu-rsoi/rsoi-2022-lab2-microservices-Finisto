package com.finist.microservices2022.libraryservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "library_books")
@NoArgsConstructor
@AllArgsConstructor
public class LibraryBook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Integer id;


    @OneToOne
    @JoinColumn(name = "book_id")
    @Getter @Setter
    private Book bookId;

    @OneToOne
    @JoinColumn(name = "library_id")
    @Getter @Setter
    private Library libraryId;

    @Column(name = "available_count")
    @Getter @Setter
    private Integer availableCount;


}
