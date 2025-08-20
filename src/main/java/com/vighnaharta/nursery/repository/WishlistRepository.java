package com.vighnaharta.nursery.repository;

import com.vighnaharta.nursery.entity.Wishlist;
import com.vighnaharta.nursery.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    // Find wishlist by user
    Optional<Wishlist> findByUser(User user);
}
