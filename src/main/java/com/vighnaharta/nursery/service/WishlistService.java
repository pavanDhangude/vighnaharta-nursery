package com.vighnaharta.nursery.service;

import com.vighnaharta.nursery.entity.Plant;
import com.vighnaharta.nursery.entity.User;
import com.vighnaharta.nursery.entity.Wishlist;
import com.vighnaharta.nursery.repository.PlantRepository;
import com.vighnaharta.nursery.repository.WishlistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final PlantRepository plantRepository;

    public WishlistService(WishlistRepository wishlistRepository, PlantRepository plantRepository) {
        this.wishlistRepository = wishlistRepository;
        this.plantRepository = plantRepository;
    }

    // ✅ Get or create wishlist for user
    public Wishlist getOrCreateWishlist(User user) {
        return wishlistRepository.findByUser(user)
                .orElseGet(() -> {
                    Wishlist wishlist = new Wishlist();
                    wishlist.setUser(user);
                    return wishlistRepository.save(wishlist);
                });
    }

    // ✅ Add plant to wishlist
    public Wishlist addPlantToWishlist(User user, Long plantId) {
        Wishlist wishlist = getOrCreateWishlist(user);
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found: " + plantId));
        if (!wishlist.getPlants().contains(plant)) {
            wishlist.getPlants().add(plant);
            wishlistRepository.save(wishlist);
        }
        return wishlist;
    }

    // ✅ Remove plant from wishlist
    public Wishlist removePlantFromWishlist(User user, Long plantId) {
        Wishlist wishlist = getOrCreateWishlist(user);
        wishlist.getPlants().removeIf(p -> p.getId().equals(plantId));
        wishlistRepository.save(wishlist);
        return wishlist;
    }

    // ✅ Get all wishlist plants for a user
    public List<Plant> getWishlistPlants(User user) {
        return getOrCreateWishlist(user).getPlants();
    }

    // ✅ Clear entire wishlist
    public void clearWishlist(User user) {
        Wishlist wishlist = getOrCreateWishlist(user);
        wishlist.getPlants().clear();
        wishlistRepository.save(wishlist);
    }
}
