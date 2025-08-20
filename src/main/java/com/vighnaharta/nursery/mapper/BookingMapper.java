package com.vighnaharta.nursery.mapper;

import com.vighnaharta.nursery.dto.BookingResponseDTO;
import com.vighnaharta.nursery.entity.Booking;
import com.vighnaharta.nursery.entity.Payment;

public class BookingMapper {

    public static BookingResponseDTO toDTO(Booking booking) {
        BookingResponseDTO dto = new BookingResponseDTO();
        dto.setId(booking.getId());
        dto.setPlantId(booking.getPlant().getId());

        // ✅ user null ho to crash avoid karo
        if (booking.getUser() != null) {
            dto.setUserId(booking.getUser().getId());
        }

        dto.setCustomerName(booking.getCustomerName());
        dto.setCustomerEmail(booking.getCustomerEmail());
        dto.setCustomerPhone(booking.getCustomerPhone());
        dto.setQuantity(booking.getQuantity());
        dto.setTotalPrice(booking.getQuantity() * booking.getPlant().getPrice()); // calculate totalPrice
        dto.setBookingDate(booking.getBookingDate());
        dto.setStatus(booking.getStatus());

        // ✅ payment details add
        Payment payment = booking.getPayment();
        if (payment != null) {
            dto.setPaymentId(payment.getId());
            dto.setPaymentMethod(payment.getPaymentMethod());
            dto.setPaymentStatus(payment.getPaymentStatus());
        }

        return dto;
    }
}
