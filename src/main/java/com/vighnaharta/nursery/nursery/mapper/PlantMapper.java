package com.vighnaharta.nursery.mapper;

import com.vighnaharta.nursery.dto.PlantRequestDTO;
import com.vighnaharta.nursery.dto.PlantResponseDTO;
import com.vighnaharta.nursery.entity.Plant;
import org.springframework.stereotype.Component;

@Component
public class PlantMapper {

    public Plant toEntity(PlantRequestDTO dto) {
        if (dto == null) return null;

        Plant plant = new Plant();
        plant.setName(dto.getName());
        plant.setCategory(dto.getCategory());
        plant.setPrice(dto.getPrice());
        plant.setDescription(dto.getDescription());
        plant.setAvailable(dto.isAvailable());

        // ✅ imageFile check: agar naya image diya gaya hai to imageUrl ko null chhodo
        // actual image upload service me update hoga
        if (dto.getImageFile() != null && !dto.getImageFile().isEmpty()) {
            plant.setImageUrl(null); // baad me service layer me set karenge
        }

        plant.setQuantity(dto.getQuantity());
        plant.setDiscountPercent(dto.getDiscountPercent());
        plant.setRating(dto.getRating());
        plant.setReviews(dto.getReviews());
        plant.setPlantingSeason(dto.getPlantingSeason());
        plant.setOriginLocation(dto.getOriginLocation());
        plant.setBookByUserId(dto.getBookByUserId());

        return plant;
    }

    public PlantResponseDTO toResponse(Plant plant) {
        if (plant == null) return null;

        PlantResponseDTO response = new PlantResponseDTO();
        response.setId(plant.getId());
        response.setName(plant.getName());
        response.setCategory(plant.getCategory());
        response.setPrice(plant.getPrice());
        response.setDescription(plant.getDescription());
        response.setAvailable(plant.isAvailable());
        response.setImageUrl(plant.getImageUrl());
        response.setQuantity(plant.getQuantity());
        response.setDiscountPercent(plant.getDiscountPercent());
        response.setRating(plant.getRating());
        response.setReviews(plant.getReviews());
        response.setPlantingSeason(plant.getPlantingSeason());
        response.setOriginLocation(plant.getOriginLocation());
        response.setBookByUserId(plant.getBookByUserId());
        response.setCreatedAt(plant.getCreatedAt());
        response.setUpdatedAt(plant.getUpdatedAt());

        return response;
    }
}
