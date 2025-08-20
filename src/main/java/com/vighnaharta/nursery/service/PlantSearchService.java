package com.vighnaharta.nursery.service;

import com.vighnaharta.nursery.entity.Plant;
import com.vighnaharta.nursery.repository.PlantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantSearchService {

    private final PlantRepository plantRepository;

    // Constructor Injection - isse PlantRepository ka object service me milta hai
    public PlantSearchService(PlantRepository plantRepository) {
        this.plantRepository = plantRepository;
    }

    // ✅ Search by Name (Aap sirf plant ka naam dalke search kar sakte ho)
    // Eg: "Rose" -> "Rose Plant", "Rosemary" etc sab match karenge
    public List<Plant> searchByName(String name) {
        return plantRepository.findByNameContainingIgnoreCase(name);
    }

    // ✅ Filter by Category (Sirf ek category ka data chahiye - jaise "Fruit" ya "Flower")
    public List<Plant> filterByCategory(String category) {
        return plantRepository.findByCategoryIgnoreCase(category);
    }

    // ✅ Filter by Price Range (User min aur max price set karke search karega)
    // Example: 100 - 500 => sirf isi range ke plants aayenge
    public List<Plant> filterByPrice(Double minPrice, Double maxPrice) {
        return plantRepository.findByPriceBetween(minPrice, maxPrice);
    }

    // ✅ Combined Search + Filter
    // Matlab ek sath name, category aur price range laga ke search karna
    // Eg: name = "Rose", category = "Flower", minPrice=100, maxPrice=500
    public List<Plant> searchWithFilters(String name, String category, Double minPrice, Double maxPrice) {
        return plantRepository.searchPlants(name, category, minPrice, maxPrice);
    }
}
