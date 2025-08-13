package com.vighnaharta.nursery.service;

import com.vighnaharta.nursery.dto.PlantRequestDTO;
import com.vighnaharta.nursery.dto.PlantResponseDTO;

import jakarta.validation.Valid;
import java.util.List;

public interface PlantService {

    PlantResponseDTO savePlant(PlantRequestDTO plantRequestDTO);

    List<PlantResponseDTO> addAllPlants(List<PlantRequestDTO> plantRequestDTOs);

    List<PlantResponseDTO> getAllPlants();

    PlantResponseDTO getPlantById(Long id);

    PlantResponseDTO updatePlant(Long id, PlantRequestDTO plantRequestDTO);

    List<PlantResponseDTO> updateMultiplePlants(List<PlantRequestDTO> plantRequestDTOs);

    void deletePlant(Long id);

    void deleteMultiplePlants(List<Long> ids);
}
