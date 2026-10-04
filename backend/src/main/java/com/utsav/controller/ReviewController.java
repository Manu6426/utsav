package com.utsav.controller;

import com.utsav.dto.CreateReviewRequest;
import com.utsav.model.Review;
import com.utsav.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Reviews, nested under their vendor: /api/vendors/{id}/reviews.
 */
@RestController
@RequestMapping("/api/vendors/{id}/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<Review> list(@PathVariable("id") String vendorId) {
        return reviewService.forVendor(vendorId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Review add(@PathVariable("id") String vendorId,
                      @Valid @RequestBody CreateReviewRequest request) {
        return reviewService.add(vendorId, request);
    }
}
