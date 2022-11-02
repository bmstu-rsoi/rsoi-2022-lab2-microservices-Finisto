package com.finist.microservices2022.gatewayapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryResponse {

    public UUID libraryUid;

    public String name;

    public String address;

    public String city;



//    public LibraryResponse(UUID libraryUid, String name, String address, String city) {
//        this.libraryUid = libraryUid;
//        this.name = name;
//        this.address = address;
//        this.city = city;
//    }
}
