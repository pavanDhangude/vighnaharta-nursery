package com.vighnaharta.nursery.controller;

import com.vighnaharta.nursery.entity.DiscountCoupon;
import com.vighnaharta.nursery.service.DiscountCouponService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/coupons")
public class DiscountCouponController {

    private final DiscountCouponService couponService;

    public DiscountCouponController(DiscountCouponService couponService) {
        this.couponService = couponService;
    }

    // 🔹 Create new coupon (Admin use)
    @PostMapping("/create")
    public ResponseEntity<DiscountCoupon> createCoupon(@RequestBody DiscountCoupon coupon) {
        DiscountCoupon savedCoupon = couponService.createCoupon(coupon);
        return ResponseEntity.ok(savedCoupon);
    }

    // 🔹 Validate and apply coupon
    @PostMapping("/apply")
    public ResponseEntity<?> applyCoupon(@RequestParam String code, @RequestParam Double totalAmount) {
        try {
            Double finalAmount = couponService.applyCoupon(code, totalAmount);
            return ResponseEntity.ok("Discount applied! Final amount: " + finalAmount);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
