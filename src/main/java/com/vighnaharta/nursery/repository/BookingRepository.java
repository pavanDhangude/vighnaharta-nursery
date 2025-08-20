package com.vighnaharta.nursery.repository;

import com.vighnaharta.nursery.entity.Booking;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByCustomerEmail(String customerEmail);
	Optional<Booking> findByPaymentOrderId(String paymentOrderId);
	
	
}
