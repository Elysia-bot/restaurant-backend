package com.restaurant.repository;

import com.restaurant.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    Optional<RestaurantTable> findByQrCode(String qrCode);
    Optional<RestaurantTable> findByTableNumber(Integer tableNumber);
    boolean existsByTableNumber(Integer tableNumber);
}
