package com.finist.microservices2022.gatewayapi.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TakeBookRequest {


    String bookUid;

    String libraryUid;

    Date tillDate;
}
