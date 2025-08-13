package com.vighnaharta.nursery.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlantRequestDTO {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be at least 1")
    private Double price;

    private String description;
    private boolean available;

    private Integer quantity;
    private Double discountPercent;
    private Double rating;
    private String reviews;
    private String plantingSeason;
    private String originLocation;
    private Long bookByUserId;

    // ✅ नया field image upload के लिए
    private MultipartFile imageFile;
}
