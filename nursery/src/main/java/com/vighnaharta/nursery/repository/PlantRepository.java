package com.vighnaharta.nursery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vighnaharta.nursery.entity.Plant;

public interface PlantRepository extends JpaRepository<Plant, Long> {
	


}

