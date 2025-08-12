package com.vighnaharta.nursery.controller;

import com.vighnaharta.nursery.entity.Booking;
import com.vighnaharta.nursery.service.BookingService;

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

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    
 // ✅ User apni booking history dekhe
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my-bookings")
    public ResponseEntity<List<Booking>> getMyBookings(Principal principal) {
        String email = principal.getName(); // login kiya hua user ka email
        List<Booking> bookings = bookingService.getBookingsForCustomerEmail(email);
        return ResponseEntity.ok(bookings);
    }
    
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
   

}
