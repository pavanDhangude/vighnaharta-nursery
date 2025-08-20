package com.vighnaharta.nursery.repository;

import com.vighnaharta.nursery.entity.Cart;
import com.vighnaharta.nursery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // User ka cart nikalne ke liye
    Optional<Cart> findByUser(User user);
}
