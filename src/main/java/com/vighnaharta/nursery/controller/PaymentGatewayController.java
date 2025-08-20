package com.vighnaharta.nursery.controller;

import com.razorpay.Order;
import com.vighnaharta.nursery.entity.Booking;
import com.vighnaharta.nursery.entity.Payment;
import com.vighnaharta.nursery.service.BookingService;
import com.vighnaharta.nursery.service.PaymentService;
import com.vighnaharta.nursery.service.RazorpayService;
import com.vighnaharta.nursery.util.PaymentVerificationUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/payments/razorpay")
public class PaymentGatewayController {

    private final RazorpayService razorpayService;
    private final BookingService bookingService;
    private final PaymentService paymentService;

    public PaymentGatewayController(RazorpayService razorpayService,
                                    BookingService bookingService,
                                    PaymentService paymentService) {
        this.razorpayService = razorpayService;
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

    // 🔹 Step A: Create Razorpay Order (frontend ko orderId + key dena)
    @PostMapping("/create-order/{bookingId}")
    public ResponseEntity<?> createOrder(@PathVariable Long bookingId) {
        try {
            Booking booking = bookingService.getBookingEntityById(bookingId);
            long amount = Math.round(booking.getTotalPrice()); // rupees
            Order order = razorpayService.createOrder(amount, "receipt_" + bookingId);

            // booking me orderId save karo for verification
            booking.setPaymentOrderId(order.get("id"));
            bookingService.saveBooking(booking);

            Map<String, Object> resp = new HashMap<>();
            resp.put("orderId", order.get("id"));
            resp.put("amount", order.get("amount"));   // paise
            resp.put("currency", order.get("currency"));
            resp.put("key", razorpayService.getKey()); // frontend ke liye
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Create order failed: " + e.getMessage());
        }
    }

 // 🔹 Step B: Verify Payment (frontend Razorpay checkout se mila data bheजे)
    @PostMapping("/verify")
    public ResponseEntity<?> verify(@RequestBody Map<String, String> payload) {
        String orderId = payload.get("razorpay_order_id");
        String paymentId = payload.get("razorpay_payment_id");
        String signature = payload.get("razorpay_signature");

        try {
            boolean ok = PaymentVerificationUtil.verifySignature(
                    orderId, paymentId, signature, razorpayService.getSecret());

            if (!ok) return ResponseEntity.badRequest().body("Invalid signature");

            // orderId se booking nikaalo
            Booking booking = bookingService.findByPaymentOrderId(orderId)
                    .orElseThrow(() -> new RuntimeException("Booking not found for orderId: " + orderId));

            // payment create + link + booking confirm + stock reduce
            Payment payment = paymentService.createOnlinePaymentAndLink(
                    booking, paymentId, orderId, booking.getTotalPrice());

            // invoice mail
            paymentService.handlePostPaymentSuccess(payment);

            Map<String, Object> resp = new HashMap<>();
            resp.put("status", "SUCCESS");
            resp.put("bookingId", booking.getId());
            resp.put("paymentId", payment.getId());
            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Verification failed: " + e.getMessage());
        }
    }
}
