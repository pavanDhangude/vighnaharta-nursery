package com.vighnaharta.nursery.repository;

import com.vighnaharta.nursery.entity.DiscountCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DiscountCouponRepository extends JpaRepository<DiscountCoupon, Long> {

    // ✅ Coupon code ke basis pe nikalne ke liye
    Optional<DiscountCoupon> findByCode(String code);
}
