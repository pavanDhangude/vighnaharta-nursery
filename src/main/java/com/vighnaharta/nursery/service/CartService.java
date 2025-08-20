package com.vighnaharta.nursery.service;

import com.vighnaharta.nursery.entity.Cart;
import com.vighnaharta.nursery.entity.CartItem;
import com.vighnaharta.nursery.entity.Plant;
import com.vighnaharta.nursery.entity.User;
import com.vighnaharta.nursery.repository.CartItemRepository;
import com.vighnaharta.nursery.repository.CartRepository;
import com.vighnaharta.nursery.repository.PlantRepository;
import com.vighnaharta.nursery.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       PlantRepository plantRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.plantRepository = plantRepository;
    }

    // ✅ User ka cart nikalna ya naya create karna
    public Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user).orElseGet(() -> {
            Cart cart = new Cart();
            cart.setUser(user);
            return cartRepository.save(cart);
        });
    }

    // ✅ Add plant to cart
    @Transactional
    public Cart addToCart(Long userId, Long plantId, int quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new RuntimeException("Plant not found"));

        Cart cart = getOrCreateCart(user);

        // Check if plant already in cart
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(i -> i.getPlant().getId().equals(plantId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setPlant(plant);
            item.setQuantity(quantity);
            item.setPrice(plant.getPrice());
            cart.getItems().add(item);
        }

        return cartRepository.save(cart);
    }

    // ✅ Remove plant from cart
    @Transactional
    public Cart removeFromCart(Long userId, Long plantId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Cart cart = getOrCreateCart(user);
        cart.getItems().removeIf(item -> item.getPlant().getId().equals(plantId));

        return cartRepository.save(cart);
    }

    // ✅ Clear entire cart
    @Transactional
    public void clearCart(User user) {
        Cart cart = getOrCreateCart(user);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
}
