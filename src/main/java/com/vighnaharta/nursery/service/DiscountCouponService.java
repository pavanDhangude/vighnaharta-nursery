package com.vighnaharta.nursery.service;

import com.vighnaharta.nursery.entity.DiscountCoupon;
import com.vighnaharta.nursery.repository.DiscountCouponRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class DiscountCouponService {

    private final DiscountCouponRepository couponRepository;

    public DiscountCouponService(DiscountCouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    // 🔹 Add new coupon
    public DiscountCoupon createCoupon(DiscountCoupon coupon) {
        return couponRepository.save(coupon);
    }

    // 🔹 Validate coupon
    public DiscountCoupon validateCoupon(String code) {
        DiscountCoupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid coupon code"));

        if (!coupon.getActive() || coupon.getUsedCount() >= coupon.getMaxUsage()) {
            throw new RuntimeException("Coupon expired or max usage reached");
        }

        LocalDateTime now = LocalDateTime.now();
        if ((coupon.getStartDate() != null && now.isBefore(coupon.getStartDate())) ||
            (coupon.getEndDate() != null && now.isAfter(coupon.getEndDate()))) {
            throw new RuntimeException("Coupon not valid at this time");
        }

        return coupon;
    }

    // 🔹 Apply coupon to total amount
    @Transactional
    public Double applyCoupon(String code, Double totalAmount) {
        DiscountCoupon coupon = validateCoupon(code);
        Double discount = totalAmount * (coupon.getDiscountPercent() / 100);
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        if (coupon.getUsedCount() >= coupon.getMaxUsage()) {
            coupon.setActive(false);
        }
        couponRepository.save(coupon);
        return totalAmount - discount;
    }
}
