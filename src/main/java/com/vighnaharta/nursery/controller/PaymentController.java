
package com.vighnaharta.nursery.controller;

import com.vighnaharta.nursery.dto.PaymentRequestDTO;
import com.vighnaharta.nursery.dto.PaymentResponseDTO;
import com.vighnaharta.nursery.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // ✅ COD initiate
    @PostMapping("/cod")
    public PaymentResponseDTO cod(@Valid @RequestBody PaymentRequestDTO dto) {
        return paymentService.createCODPayment(dto);
    }

    // 🔎 Get payment by booking
    @GetMapping("/booking/{bookingId}")
    public PaymentResponseDTO getByBooking(@PathVariable Long bookingId) {
        return paymentService.getPaymentByBooking(bookingId);
    }
}
