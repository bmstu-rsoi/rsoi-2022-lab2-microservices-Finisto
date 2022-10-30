package com.finist.microservices2022.gatewayapi.model;

public class LibraryResponse {

    public String libraryUid;

    public String name;

    public String address;

    public String city;

    public LibraryResponse(String libraryUid, String name, String address, String city) {
        this.libraryUid = libraryUid;
        this.name = name;
        this.address = address;
        this.city = city;
    }
}
