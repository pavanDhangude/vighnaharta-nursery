package com.vighnaharta.nursery.service;

import com.vighnaharta.nursery.entity.Plant;
import com.vighnaharta.nursery.repository.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ImageUploadService {

    // ✅ Image को uploads folder में store करने का path
    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private PlantRepository plantRepository;

    public String uploadPlantImage(Long plantId, MultipartFile file) throws IOException {

        // ✅ Plant को database से fetch करो
        Optional<Plant> plantOptional = plantRepository.findById(plantId);
        if (plantOptional.isEmpty()) {
            throw new RuntimeException("Plant not found with ID: " + plantId);
        }

        Plant plant = plantOptional.get();

        // ✅ अगर uploads folder नहीं है तो बना दो
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // ✅ Image file का unique नाम generate करो
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // ✅ Image को uploads/ folder में save करो
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        // ✅ Plant entity में imageUrl डाल दो और save करो
        plant.setImageUrl(filePath.toString());
        plant.setUpdatedAt(LocalDateTime.now());
        plantRepository.save(plant);

        return filePath.toString(); // return image path
    }
    public void deleteImage(String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) return;

        try {
            Path path = Paths.get(imagePath);
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image: " + e.getMessage());
        }
    }

    
}
