package com.restaurant.repository;

import com.restaurant.entity.Order;
import com.restaurant.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Tất cả orders của 1 bàn (guest xem lịch sử gọi món của mình)
    List<Order> findByTableIdOrderByCreatedAtDesc(Long tableId);

    // Admin filter orders theo status
    List<Order> findByStatusOrderByCreatedAtAsc(OrderStatus status);

    // Admin xem tất cả orders, mới nhất lên đầu
    List<Order> findAllByOrderByCreatedAtDesc();

    Order findByTableIdAndStatus(Long tableId, OrderStatus status);

    // Doanh thu theo khoảng thời gian (chỉ tính COMPLETED)
    @Query("""
        SELECT COALESCE(SUM(o.totalPrice), 0)
        FROM Order o
        WHERE o.status = 'COMPLETED'
          AND o.createdAt BETWEEN :from AND :to
        """)
    BigDecimal calculateRevenue(@Param("from") LocalDateTime from,
                                @Param("to") LocalDateTime to);

    // Đếm số order theo status trong khoảng thời gian
    @Query("""
        SELECT o.status, COUNT(o)
        FROM Order o
        WHERE o.createdAt BETWEEN :from AND :to
        GROUP BY o.status
        """)
    List<Object[]> countByStatusBetween(@Param("from") LocalDateTime from,
                                        @Param("to") LocalDateTime to);
}
