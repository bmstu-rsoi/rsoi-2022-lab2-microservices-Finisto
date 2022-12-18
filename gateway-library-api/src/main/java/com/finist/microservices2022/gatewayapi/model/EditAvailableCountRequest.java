package com.finist.microservices2022.gatewayapi.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditAvailableCountRequest {

    String bookUid;

    Integer byCount;
}
