package com.vighnaharta.nursery.controller;

import com.vighnaharta.nursery.entity.Cart;
import com.vighnaharta.nursery.service.CartService;
import com.vighnaharta.nursery.entity.User;
import com.vighnaharta.nursery.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    // ✅ Add to cart
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam Long userId,
                                       @RequestParam Long plantId,
                                       @RequestParam int quantity) {
        Cart cart = cartService.addToCart(userId, plantId, quantity);
        return ResponseEntity.ok(cart);
    }

    // ✅ Remove from cart
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFromCart(@RequestParam Long userId,
                                            @RequestParam Long plantId) {
        Cart cart = cartService.removeFromCart(userId, plantId);
        return ResponseEntity.ok(cart);
    }

    // ✅ View cart
    @GetMapping("/view")
    public ResponseEntity<?> viewCart(@RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartService.getOrCreateCart(user);
        return ResponseEntity.ok(cart);
    }

    // ✅ Clear cart
    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestParam Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        cartService.clearCart(user);
        return ResponseEntity.ok("Cart cleared successfully");
    }
}
