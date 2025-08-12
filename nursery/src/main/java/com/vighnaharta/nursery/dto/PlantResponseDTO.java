package com.vighnaharta.nursery.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlantResponseDTO {

    private Long id;
    private String name;
    private String category;
    private Double price;
    private String description;
    private boolean available;
    private String imageUrl;
    private Integer quantity;
    private Double discountPercent;
    private Double rating;
    private String reviews;
    private String plantingSeason;
    private String originLocation;
    private Long bookByUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
