package com.vighnaharta.nursery.dto;

import com.vighnaharta.nursery.enums.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDTO {
    @NotNull(message = "bookingId required")
    private Long bookingId;

    @NotNull @Min(value = 0, message = "amount must be >= 0")
    private Double amount;

    @NotNull
    private PaymentMethod paymentMethod; // COD ya ONLINE
}
