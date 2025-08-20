package com.vighnaharta.nursery.dto;

import com.vighnaharta.nursery.enums.PaymentMethod;
import com.vighnaharta.nursery.enums.PaymentStatus;
import lombok.Data;

@Data
public class PaymentResponseDTO {
    private Long paymentId;
    private Long bookingId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
}
