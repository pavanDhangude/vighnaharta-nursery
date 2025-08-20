package com.vighnaharta.nursery.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Entity
@Table(name = "discount_coupons")
public class DiscountCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Coupon code (unique)
    @Column(nullable = false, unique = true)
    private String code;

    // ✅ Discount percentage (0-100)
    @Column(nullable = false)
    private Double discountPercent;

    // ✅ Coupon valid from
    private LocalDateTime startDate;

    // ✅ Coupon valid till
    private LocalDateTime endDate;

    // ✅ Maximum number of uses for this coupon
    private Integer maxUsage;

    // ✅ Number of times coupon has been used
    private Integer usedCount = 0;

    // ✅ Is coupon active or expired
    private Boolean active = true;

    @PrePersist
    public void prePersist() {
        if (usedCount == null) usedCount = 0;
        if (active == null) active = true;
    }

    // Getters & Setters
    // ...
}
