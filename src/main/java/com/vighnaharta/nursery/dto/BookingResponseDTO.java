package com.vighnaharta.nursery.dto;

import java.time.LocalDateTime;
import com.vighnaharta.nursery.enums.PaymentMethod;
import com.vighnaharta.nursery.enums.PaymentStatus;
import lombok.Data;

@Data
public class BookingResponseDTO {
    private Long id;
    private Long plantId;
    private Long userId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private int quantity;
    private double totalPrice;
    private LocalDateTime bookingDate;
    private String status;

    // Payment fields
    private Long paymentId;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;

    // ✅ Proper setters for payment
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    // Customer info setters (if needed)
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public void setStatus(String status) { this.status = status; }
}
