package com.vighnaharta.nursery.controller;

import com.vighnaharta.nursery.entity.Plant;
import com.vighnaharta.nursery.entity.User;
import com.vighnaharta.nursery.service.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    // ✅ Add plant to wishlist
    @PostMapping("/add/{plantId}")
    public ResponseEntity<List<Plant>> addToWishlist(@AuthenticationPrincipal User user,
                                                     @PathVariable Long plantId) {
        List<Plant> plants = wishlistService.addPlantToWishlist(user, plantId).getPlants();
        return ResponseEntity.ok(plants);
    }

    // ✅ Remove plant from wishlist
    @DeleteMapping("/remove/{plantId}")
    public ResponseEntity<List<Plant>> removeFromWishlist(@AuthenticationPrincipal User user,
                                                          @PathVariable Long plantId) {
        List<Plant> plants = wishlistService.removePlantFromWishlist(user, plantId).getPlants();
        return ResponseEntity.ok(plants);
    }

    // ✅ Get all wishlist plants
    @GetMapping
    public ResponseEntity<List<Plant>> getWishlist(@AuthenticationPrincipal User user) {
        List<Plant> plants = wishlistService.getWishlistPlants(user);
        return ResponseEntity.ok(plants);
    }

    // ✅ Clear wishlist
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearWishlist(@AuthenticationPrincipal User user) {
        wishlistService.clearWishlist(user);
        return ResponseEntity.ok("Wishlist cleared successfully!");
    }
}
