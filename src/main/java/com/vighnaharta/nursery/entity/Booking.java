package com.vighnaharta.nursery.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vighnaharta.nursery.dto.BookingResponseDTO;

@Data

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Customer email is required")
    private String customerEmail;

    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String customerPhone;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    private Plant plant;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    private LocalDateTime bookingDate;

    private String status;
    
 // NEW: razorpay order id yahan temporarily store karenge verification ke liye
    private String paymentOrderId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;  // ✅ add this

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonManagedReference   // 👈 yaha lagana hai
    private Payment payment;


    @PrePersist
    public void prePersist() {
        if (this.bookingDate == null) {
            this.bookingDate = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = "PENDING";
        }
    }

	public double getTotalPrice() {
		// TODO Auto-generated method stub
		return plant != null ? quantity * plant.getPrice() : 0;
		
	}

	

    // getters and setters here
    // Use Lombok if possible (@Getter @Setter)
}
