package com.vighnaharta.nursery.service.impl;

import com.vighnaharta.nursery.dto.PlantRequestDTO;
import com.vighnaharta.nursery.dto.PlantResponseDTO;
import com.vighnaharta.nursery.entity.Plant;
import com.vighnaharta.nursery.mapper.PlantMapper;
import com.vighnaharta.nursery.repository.PlantRepository;
import com.vighnaharta.nursery.service.PlantService;
import com.vighnaharta.nursery.service.ImageUploadService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PlantServiceImpl implements PlantService {

    private final PlantRepository plantRepository;
    private final ImageUploadService imageUploadService;
    private final PlantMapper plantMapper; // ✅ injected

    @Override
    public List<PlantResponseDTO> getAllPlants() {
        return plantRepository.findAll().stream()
                .map(plantMapper::toResponse) // ✅ FIXED
                .toList();
    }

    @Override
    public PlantResponseDTO getPlantById(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found with id: " + id));
        return plantMapper.toResponse(plant); // ✅ FIXED
    }

    @Override
	public PlantResponseDTO savePlant(@ModelAttribute PlantRequestDTO plantDTO) {
	    Plant plant = plantMapper.toEntity(plantDTO);

	    // ✅ पहले plant को save करो ताकि ID मिल जाए
	    Plant savedPlant = plantRepository.save(plant);

	    MultipartFile imageFile = plantDTO.getImageFile();
	    if (imageFile != null && !imageFile.isEmpty()) {
	        try {
	            // ✅ अब image upload करो with plant ID
	            String imagePath = imageUploadService.uploadPlantImage(savedPlant.getId(), imageFile);

	            // ✅ image path ko set karo plant entity में
	            savedPlant.setImageUrl(imagePath);

	            // ✅ दुबारा save करो updated plant
	            savedPlant = plantRepository.save(savedPlant);

	        } catch (IOException e) {
	            throw new RuntimeException("Image upload failed: " + e.getMessage());
	        }
	    }

	    return plantMapper.toResponse(savedPlant);
	}
    
    
    @Override
    public PlantResponseDTO updatePlant(@PathVariable Long id,@ModelAttribute PlantRequestDTO plantDTO) {
        Plant existingPlant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found with id: " + id));

        // ✅ पुरानी image path याद रखो
        String oldImagePath = existingPlant.getImageUrl();

        // ✅ बाकी fields update करो
        existingPlant.setName(plantDTO.getName());
        existingPlant.setCategory(plantDTO.getCategory());
        existingPlant.setPrice(plantDTO.getPrice());
        existingPlant.setDescription(plantDTO.getDescription());
        existingPlant.setAvailable(plantDTO.isAvailable());
        existingPlant.setQuantity(plantDTO.getQuantity());
        existingPlant.setDiscountPercent(plantDTO.getDiscountPercent());
        existingPlant.setRating(plantDTO.getRating());
        existingPlant.setReviews(plantDTO.getReviews());
        existingPlant.setPlantingSeason(plantDTO.getPlantingSeason());
        existingPlant.setOriginLocation(plantDTO.getOriginLocation());
        existingPlant.setBookByUserId(plantDTO.getBookByUserId());

        MultipartFile newImage = plantDTO.getImageFile();

        if (newImage != null && !newImage.isEmpty()) {
            try {
                // ✅ पुरानी image delete करो
                imageUploadService.deleteImage(oldImagePath);

                // ✅ नई image upload करो
                String newImagePath = imageUploadService.uploadPlantImage(id, newImage);

                // ✅ नई image path set करो
                existingPlant.setImageUrl(newImagePath);

            } catch (IOException e) {
                throw new RuntimeException("Image update failed: " + e.getMessage());
            }
        }

        Plant updatedPlant = plantRepository.save(existingPlant);
        return plantMapper.toResponse(updatedPlant);
    }


    @Override
    public void deletePlant(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found with id: " + id));

        // ✅ अगर image है, तो उसे भी delete करो
        if (plant.getImageUrl() != null) {
            imageUploadService.deleteImage(plant.getImageUrl());
        }

        // ✅ फिर plant को delete करो
        plantRepository.deleteById(id);
    }

	@Override
	public List<PlantResponseDTO> addAllPlants(List<PlantRequestDTO> plantRequestDTOs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PlantResponseDTO> updateMultiplePlants(List<PlantRequestDTO> plantRequestDTOs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMultiplePlants(List<Long> ids) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	

}
