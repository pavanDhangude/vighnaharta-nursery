package com.vighnaharta.nursery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "plant_reviews")
public class PlantReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Plant ke liye review
    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    // ✅ User jo review kar raha hai
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // ✅ Rating (1 to 5)
    private Integer rating;

    // ✅ Optional comment
    @Column(length = 500)
    private String comment;

    // ✅ Review ka timestamp
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    // Getters & Setters
    // ...
}
