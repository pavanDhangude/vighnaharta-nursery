package com.vighnaharta.nursery.repository;

import com.vighnaharta.nursery.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
