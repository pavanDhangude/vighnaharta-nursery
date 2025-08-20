package com.vighnaharta.nursery.controller;

import com.vighnaharta.nursery.entity.PlantReview;
import com.vighnaharta.nursery.service.PlantReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class PlantReviewController {

    private final PlantReviewService reviewService;

    public PlantReviewController(PlantReviewService reviewService) {
        this.reviewService = reviewService;
    }

    // 🔹 Add a review
    @PostMapping("/add")
    public ResponseEntity<PlantReview> addReview(@RequestParam Long plantId,
                                                 @RequestParam Long userId,
                                                 @RequestParam Integer rating,
                                                 @RequestParam(required = false) String comment) {
        PlantReview review = reviewService.addReview(plantId, userId, rating, comment);
        return ResponseEntity.ok(review);
    }

    // 🔹 Get all reviews of a plant
    @GetMapping("/plant/{plantId}")
    public ResponseEntity<List<PlantReview>> getReviews(@PathVariable Long plantId) {
        List<PlantReview> reviews = reviewService.getReviewsByPlant(plantId);
        return ResponseEntity.ok(reviews);
    }

    // 🔹 Delete a review
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }
}
