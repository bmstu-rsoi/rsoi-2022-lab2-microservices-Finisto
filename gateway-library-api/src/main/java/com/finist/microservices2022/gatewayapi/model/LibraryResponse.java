package com.finist.microservices2022.gatewayapi.model;

import java.util.UUID;

public class LibraryResponse {

    public UUID libraryUid;

    public String name;

    public String address;

    public String city;

    public LibraryResponse(UUID libraryUid, String name, String address, String city) {
        this.libraryUid = libraryUid;
        this.name = name;
        this.address = address;
        this.city = city;
    }
}
