package com.finist.microservices2022.ratingservice.repository;

import com.finist.microservices2022.ratingservice.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    public Rating getRatingByUsername(String username);
}
