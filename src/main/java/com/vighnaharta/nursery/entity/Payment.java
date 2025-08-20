
package com.vighnaharta.nursery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vighnaharta.nursery.enums.PaymentMethod;
import com.vighnaharta.nursery.enums.PaymentStatus;

@Getter @Setter
@Entity
@Table(name = "payments",
       uniqueConstraints = @UniqueConstraint(name = "uk_payment_booking", columnNames = "booking_id"))
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "booking_id")
    @JsonBackReference   
    private Booking booking;


    // Kitna amount pay hona hai
    @Column(nullable = false)
    private Double amount;

    // COD / ONLINE / UPI / WALLET / EMI
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentMethod paymentMethod;

    // PENDING / SUCCESS / FAILED / REFUNDED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    // Online gateway ka txn ya order id (COD mēn null)
    @Column(unique = true,length = 100)
    private String transactionId;
    // NEW: Razorpay order_id (orderId)
    private String orderId;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
