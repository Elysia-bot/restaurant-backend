package com.restaurant.repository;

import com.restaurant.entity.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    // Tìm theo category
    List<MenuItem> findByCategoryIdAndIsAvailableTrue(Long categoryId);

    // Tìm theo tên (không phân biệt hoa thường)
    List<MenuItem> findByNameContainingIgnoreCaseAndIsAvailableTrue(String name);

    // Filter kết hợp: search + category + khoảng giá
    @Query("""
        SELECT m FROM MenuItem m
        WHERE m.isAvailable = true
          AND (:name IS NULL OR LOWER(m.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:categoryId IS NULL OR m.category.id = :categoryId)
          AND (:minPrice IS NULL OR m.price >= :minPrice)
          AND (:maxPrice IS NULL OR m.price <= :maxPrice)
        ORDER BY m.category.displayOrder, m.id
        """)
    List<MenuItem> searchMenuItems(
            @Param("name") String name,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );

    // Top món bán chạy
    @Query("""
        SELECT m, SUM(oi.quantity) as totalSold
        FROM OrderItem oi
        JOIN oi.menuItem m
        GROUP BY m
        ORDER BY totalSold DESC
        """)
    List<Object[]> findTopSellingItems();
}
