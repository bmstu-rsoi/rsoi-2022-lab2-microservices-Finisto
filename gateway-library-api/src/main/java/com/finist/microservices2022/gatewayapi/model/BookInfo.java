package com.finist.microservices2022.gatewayapi.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookInfo {

    String bookUid;

    String name;

    String author;

    String genre;
}
