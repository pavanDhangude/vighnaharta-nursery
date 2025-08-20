package com.vighnaharta.nursery.controller;

import com.vighnaharta.nursery.entity.Plant;
import com.vighnaharta.nursery.service.PlantSearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/plants/search")
public class PlantSearchController {

    private final PlantSearchService searchService;

    public PlantSearchController(PlantSearchService searchService) {
        this.searchService = searchService;
    }

    // Search by name → /api/plants/search/name?query=rose
    @GetMapping("/name")
    public List<Plant> searchByName(@RequestParam String query) {
        return searchService.searchByName(query);
    }

    // Filter by category → /api/plants/search/category?category=flower
    @GetMapping("/category")
    public List<Plant> filterByCategory(@RequestParam String category) {
        return searchService.filterByCategory(category);
    }

    // Filter by price → /api/plants/search/price?min=100&max=500
    @GetMapping("/price")
    public List<Plant> filterByPrice(@RequestParam Double min, @RequestParam Double max) {
        return searchService.filterByPrice(min, max);
    }

    // Combined search + filter → /api/plants/search/advance?name=rose&category=flower&min=100&max=500
    @GetMapping("/advance")
    public List<Plant> searchWithFilters(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Double min,
            @RequestParam(required = false) Double max
    ) {
        return searchService.searchWithFilters(name, category, min, max);
    }
}
