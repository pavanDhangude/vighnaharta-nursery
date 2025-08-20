package com.vighnaharta.nursery.service;

import com.vighnaharta.nursery.dto.BookingResponseDTO;
import com.vighnaharta.nursery.entity.Booking;
import com.vighnaharta.nursery.entity.Plant;
import com.vighnaharta.nursery.mapper.BookingMapper;
import com.vighnaharta.nursery.repository.BookingRepository;
import com.vighnaharta.nursery.repository.PlantRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final JavaMailSender mailSender;
    private final BookingRepository bookingRepository;
    private final PlantRepository plantRepository;

    public BookingService(JavaMailSender mailSender, BookingRepository bookingRepository, PlantRepository plantRepository) {
        this.mailSender = mailSender;
        this.bookingRepository = bookingRepository;
        this.plantRepository = plantRepository;
    }

    @Transactional
    public Booking createBooking(Long plantId, Booking booking) {
        Plant plant = plantRepository.findById(plantId)
                .orElseThrow(() -> new IllegalArgumentException("Plant not found with id: " + plantId));

        if (!plant.isAvailable()) {
            throw new IllegalStateException("Plant is not available for booking");
        }

        booking.setPlant(plant);
        booking.setStatus("PENDING");
        // bookingDate auto-set by @PrePersist

        Booking savedBooking = bookingRepository.save(booking);

        // Email bhejte waqt exception handle karo
        try {
            sendEmail(savedBooking.getCustomerEmail(),
                    "Booking Confirmation - Vighnaharta Nursery",
                    "Dear " + savedBooking.getCustomerName() + ",\n\n" +
                            "Thank you for booking '" + plant.getName() + "'. Your booking is currently pending approval.\n\n" +
                            "Regards,\nVighnaharta Nursery Team");
        } catch (Exception e) {
            System.err.println("Booking confirmation email failed to send: " + e.getMessage());
            // Exception ko swallow karo taki transaction fail na ho
        }

        return savedBooking;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Transactional
    public Booking updateStatus(Long bookingId, String status) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));

        // Validate status value
        String upperStatus = status.toUpperCase();
        if (!upperStatus.equals("PENDING") && !upperStatus.equals("CONFIRMED") && !upperStatus.equals("CANCELLED") && !upperStatus.equals("REJECTED")) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }

        booking.setStatus(upperStatus);
        bookingRepository.save(booking);

        String message = switch (upperStatus) {
            case "CONFIRMED" -> "Your booking for '" + booking.getPlant().getName() + "' has been confirmed.";
            case "REJECTED" -> "Sorry, your booking for '" + booking.getPlant().getName() + "' has been rejected.";
            case "CANCELLED" -> "Your booking for '" + booking.getPlant().getName() + "' has been cancelled.";
            default -> "Your booking status has been updated to " + upperStatus + ".";
        };

        try {
            sendEmail(booking.getCustomerEmail(),
                    "Booking Status Update - Vighnaharta Nursery",
                    "Dear " + booking.getCustomerName() + ",\n\n" + message + "\n\nRegards,\nVighnaharta Nursery Team");
        } catch (Exception e) {
            System.err.println("Booking status update email failed to send: " + e.getMessage());
        }

        return booking;
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom("pavandhangude28@gmail.com"); // Use your verified email
        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(text);
        mailSender.send(mail);
    }

    @Transactional
    public void deleteBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + bookingId));
        bookingRepository.delete(booking);
    }
    
 // ✅ User ke liye apni booking history
    public List<Booking> getBookingsForCustomerEmail(String customerEmail) {
        return bookingRepository.findByCustomerEmail(customerEmail);
    }
    
    
    public List<BookingResponseDTO> getAllBookings1() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public BookingResponseDTO getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
        return BookingMapper.toDTO(booking);
    }

    
    
    
    
    
    
    
    public Booking getBookingEntityById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found: " + id));
    }

    public Optional<Booking> findByPaymentOrderId(String orderId) {
        return bookingRepository.findByPaymentOrderId(orderId);
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    
}
