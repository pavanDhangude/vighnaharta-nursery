package com.vighnaharta.nursery.controller;

import com.vighnaharta.nursery.dto.PlantRequestDTO;
import com.vighnaharta.nursery.dto.PlantResponseDTO;
import com.vighnaharta.nursery.service.PlantService;
import com.vighnaharta.nursery.service.ImageUploadService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/plants")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService plantService;
    private final ImageUploadService imageUploadService;

    // ✅ Anyone can access (User + Admin)
    @GetMapping
    public ResponseEntity<List<PlantResponseDTO>> getAllPlants() {
        return ResponseEntity.ok(plantService.getAllPlants());
    }

    // ✅ Anyone can access by ID
    @GetMapping("/{id}")
    public ResponseEntity<PlantResponseDTO> getPlantById(@PathVariable Long id) {
        return ResponseEntity.ok(plantService.getPlantById(id));
    }

   
    // 🔒 Only ADMIN can access
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public ResponseEntity<PlantResponseDTO> updatePlant(@PathVariable Long id, @ModelAttribute PlantRequestDTO plantDTO) {
        return ResponseEntity.ok(plantService.updatePlant(id, plantDTO));
    }

    // 🔒 Only ADMIN can access
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePlant(@PathVariable Long id) {
        plantService.deletePlant(id);
        return ResponseEntity.ok("Plant deleted successfully.");
    }

    // 🔒 ✅ Only ADMIN can upload image for a plant
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/upload-image")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String imagePath = imageUploadService.uploadPlantImage(id, file);
            return ResponseEntity.ok("Image uploaded successfully: " + imagePath);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Image upload failed: " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
 // ✅ @RequestBody hatao, @ModelAttribute use karo
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlantResponseDTO> savePlant(@ModelAttribute PlantRequestDTO dto) {
        return ResponseEntity.ok(plantService.savePlant(dto));
    }

    
    
}
