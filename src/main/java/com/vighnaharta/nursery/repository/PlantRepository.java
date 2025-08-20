package com.vighnaharta.nursery.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.vighnaharta.nursery.entity.Plant;

public interface PlantRepository extends JpaRepository<Plant, Long> {

    // 🔎 Plant ko name se search karne ke liye (case-insensitive)
    List<Plant> findByNameContainingIgnoreCase(String name);

    // 🔎 Plant ko category ke basis pe search karne ke liye (case-insensitive)
    List<Plant> findByCategoryIgnoreCase(String category);

    // 🔎 Price range ke basis pe plants search karne ke liye
    List<Plant> findByPriceBetween(Double minPrice, Double maxPrice);

    // 🔎 Custom search (optional filters ke saath) => agar name/category/price null ho to ignore karega
    @Query("SELECT p FROM Plant p " +
           "WHERE (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (:category IS NULL OR LOWER(p.category) = LOWER(:category)) " +
           "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
           "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Plant> searchPlants(
            @Param("name") String name,
            @Param("category") String category,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice
    );
}
