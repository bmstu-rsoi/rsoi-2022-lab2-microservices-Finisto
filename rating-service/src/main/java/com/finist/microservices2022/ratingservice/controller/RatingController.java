package com.finist.microservices2022.ratingservice.controller;

import com.finist.microservices2022.gatewayapi.model.ErrorResponse;
import com.finist.microservices2022.gatewayapi.model.UserRatingResponse;
import com.finist.microservices2022.ratingservice.model.Rating;
import com.finist.microservices2022.ratingservice.repository.RatingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class RatingController {

    private final RatingRepository ratingRepository;

    public RatingController(RatingRepository ratingRepository){
        this.ratingRepository = ratingRepository;
    }


    @GetMapping("/rating")
    public ResponseEntity<?> getUserRating(@RequestParam String username){
        Rating ratingEntity = ratingRepository.getRatingByUsername(username);

        if(ratingEntity != null){
            return new ResponseEntity<UserRatingResponse>(new UserRatingResponse(ratingEntity.getStars()), HttpStatus.OK);
        }
        else{
            return new ResponseEntity<ErrorResponse>(new ErrorResponse("User with username '%s' not found".formatted(username)), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/rating/edit")
    public ResponseEntity<?> editUserRatingByOffset(@RequestParam String username, @RequestParam Integer offset){
        Rating rating = ratingRepository.getRatingByUsername(username);
        int newRating = rating.getStars() + offset;
        if(newRating < 0)
            newRating = 0;
        if(newRating > 100)
            newRating = 100;

        rating.setStars(newRating);
        ratingRepository.save(rating);

        return new ResponseEntity<>(newRating, HttpStatus.OK);
    }
}
