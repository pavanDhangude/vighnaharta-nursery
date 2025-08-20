package com.vighnaharta.nursery.repository;

import com.vighnaharta.nursery.entity.PlantReview;
import com.vighnaharta.nursery.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlantReviewRepository extends JpaRepository<PlantReview, Long> {

    // ✅ Plant ke saare reviews nikalne ke liye
    List<PlantReview> findByPlant(Plant plant);
    
    // ✅ User ka review agar chahiye
    List<PlantReview> findByUserId(Long userId);
}
