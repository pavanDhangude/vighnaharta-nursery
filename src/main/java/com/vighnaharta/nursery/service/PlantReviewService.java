package com.vighnaharta.nursery.service;

import com.vighnaharta.nursery.entity.Plant;
import com.vighnaharta.nursery.entity.PlantReview;
import com.vighnaharta.nursery.entity.User;
import com.vighnaharta.nursery.repository.PlantReviewRepository;
import com.vighnaharta.nursery.repository.PlantRepository;
import com.vighnaharta.nursery.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PlantReviewService {

    private final PlantReviewRepository reviewRepository;
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    public PlantReviewService(PlantReviewRepository reviewRepository,
                              PlantRepository plantRepository,
                              UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.plantRepository = plantRepository;
        this.userRepository = userRepository;
    }

    // ✅ Add new review
    @Transactional
    public PlantReview addReview(Long plantId, Long userId, Integer rating, String comment) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PlantReview review = new PlantReview();
        review.setPlant(plant);
        review.setUser(user);
        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.save(review);
    }

    // ✅ Get all reviews of a plant
    public List<PlantReview> getReviewsByPlant(Long plantId) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found"));
        return reviewRepository.findByPlant(plant);
    }

    // ✅ Delete a review by id
    @Transactional
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}
