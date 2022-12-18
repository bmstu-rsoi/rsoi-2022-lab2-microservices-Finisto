package com.finist.microservices2022.gatewaylibraryservice.handler;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {



    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        String text = response.getStatusText();
        System.out.println(text);



//        if(response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR){
//            throw new HttpClientErrorException(response.getStatusCode());
//        } else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
//            if(response.getStatusCode() == HttpStatus.NOT_FOUND){
//                throw
//            }
//
//            throw new HttpClientErrorException(response.getStatusCode());
//        }
//        else{
//
//        }
    }
}
