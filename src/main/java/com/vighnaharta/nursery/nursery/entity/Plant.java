package com.vighnaharta.nursery.entity;

import jakarta.persistence.*;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "plants")

@NoArgsConstructor
@AllArgsConstructor
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    private String category;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be at least 1")
    @Column(nullable = false)
    private Double price;

    @Size(max = 200, message = "Description should not exceed 200 characters")
    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private boolean available;

    // ✅ Image का URL store करने के लिए field (image file server पे save होगी)
    private String imageUrl;

    // ✅ बाक़ी extra fields
    private Integer quantity;
    private Double discountPercent;
    private Double rating;
    private String reviews;
    private String plantingSeason;
    private String originLocation;
    private Long bookByUserId;

    // ✅ Timestamp fields
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ Automatically timestamps set होंगे create के समय
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ Automatically timestamps set होंगे update के समय
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
