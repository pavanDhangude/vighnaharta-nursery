package com.vighnaharta.nursery.repository;

import com.vighnaharta.nursery.entity.Booking;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
	List<Booking> findByCustomerEmail(String customerEmail);
	
	
	
}
