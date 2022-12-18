package com.finist.microservices2022.ratingservice.controller;

import com.finist.microservices2022.gatewayapi.model.UserRatingResponse;
import com.finist.microservices2022.ratingservice.model.Rating;
import com.finist.microservices2022.ratingservice.repository.RatingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class RatingController {

    private final RatingRepository ratingRepository;

    public RatingController(RatingRepository ratingRepository){
        this.ratingRepository = ratingRepository;
    }


    @GetMapping("/rating")
    public ResponseEntity<UserRatingResponse> getUserRating(@RequestParam String username){
        Rating ratingEntity = ratingRepository.getRatingByUsername(username);

        if(ratingEntity != null){
            return new ResponseEntity<UserRatingResponse>(new UserRatingResponse(ratingEntity.getStars()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
