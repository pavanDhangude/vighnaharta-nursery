package com.vighnaharta.nursery.controller;

import com.vighnaharta.nursery.dto.BookingResponseDTO;
import com.vighnaharta.nursery.dto.PaymentRequestDTO;
import com.vighnaharta.nursery.entity.Booking;
import com.vighnaharta.nursery.enums.PaymentMethod;
import com.vighnaharta.nursery.mapper.BookingMapper;
import com.vighnaharta.nursery.service.BookingService;
import com.vighnaharta.nursery.service.PaymentService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final PaymentService paymentService; 

    // ✅ Constructor injection dono services ke liye
    public BookingController(BookingService bookingService, PaymentService paymentService) {
        this.bookingService = bookingService;
        this.paymentService = paymentService;
    }

 // ✅ Booking create (USER or ADMIN)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PostMapping("/{plantId}")
    public ResponseEntity<?> createBooking(@PathVariable Long plantId,
                                           @Valid @RequestBody Booking booking) {
        try {
            Booking created = bookingService.createBooking(plantId, booking);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ✅ Get all bookings (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    // ✅ Get booking by ID (ADMIN only) → includes payment details
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponseDTO> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    // ✅ User apni booking history dekhe
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings(Principal principal) {
        String email = principal.getName(); // login kiya hua user ka email
        List<Booking> bookings = bookingService.getBookingsForCustomerEmail(email);
        return ResponseEntity.ok(bookings);
    }

    // ✅ Update booking status (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{bookingId}")
    public ResponseEntity<?> updateStatus(@PathVariable Long bookingId,
                                          @RequestParam String status) {
        try {
            Booking updated = bookingService.updateStatus(bookingId, status);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ✅ Delete booking (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookingId}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long bookingId) {
        try {
            bookingService.deleteBooking(bookingId);
            return ResponseEntity.ok("Booking deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ✅ Booking + COD Payment + Confirmation Email (USER)
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/cod")
    public BookingResponseDTO createBookingWithCOD(@RequestBody Booking booking) {
        // 1️⃣ Booking save
        Booking savedBooking = bookingService.createBooking(booking.getPlant().getId(), booking);

        // 2️⃣ COD Payment create
        PaymentRequestDTO paymentDTO = new PaymentRequestDTO();
        paymentDTO.setBookingId(savedBooking.getId());
        paymentDTO.setAmount(savedBooking.getQuantity() * savedBooking.getPlant().getPrice());
        paymentDTO.setPaymentMethod(PaymentMethod.COD);

        paymentService.createCODPayment(paymentDTO); // email bhi yahan se jayega

        // 3️⃣ Return BookingResponseDTO (mapper will include payment details)
        return BookingMapper.toDTO(savedBooking);
    }
}
