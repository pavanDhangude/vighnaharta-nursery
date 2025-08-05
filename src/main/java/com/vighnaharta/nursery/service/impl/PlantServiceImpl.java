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
    public PlantResponseDTO savePlant(PlantRequestDTO plantDTO) {
        Plant plant = plantMapper.toEntity(plantDTO); // ✅ FIXED

        MultipartFile imageFile = plantDTO.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String imagePath = imageUploadService.uploadPlantImage(null, imageFile);
                plant.setImageUrl(imagePath);
            } catch (IOException e) {
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }

        Plant saved = plantRepository.save(plant);
        return plantMapper.toResponse(saved); // ✅ FIXED
    }

    @Override
    public PlantResponseDTO updatePlant(Long id, PlantRequestDTO plantDTO) {
        Plant existingPlant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found with id: " + id));

        String oldImagePath = existingPlant.getImageUrl();

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
                imageUploadService.deleteImage(oldImagePath); // ✅ FIXED method added
                String newImagePath = imageUploadService.uploadPlantImage(id, newImage);
                existingPlant.setImageUrl(newImagePath);
            } catch (IOException e) {
                throw new RuntimeException("Image update failed: " + e.getMessage());
            }
        }

        Plant updatedPlant = plantRepository.save(existingPlant);
        return plantMapper.toResponse(updatedPlant); // ✅ FIXED
    }

    @Override
    public void deletePlant(Long id) {
        Plant plant = plantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Plant not found with id: " + id));
        if (plant.getImageUrl() != null) {
            imageUploadService.deleteImage(plant.getImageUrl()); // ✅ FIXED
        }
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
