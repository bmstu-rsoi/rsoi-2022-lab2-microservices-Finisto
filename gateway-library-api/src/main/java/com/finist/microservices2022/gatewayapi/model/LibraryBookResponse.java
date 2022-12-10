package com.finist.microservices2022.gatewayapi.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class LibraryBookResponse {

    public String bookUid;

    public String name;

    public String author;

    public String genre;

    public String condition;

    public int availableCount;
}
