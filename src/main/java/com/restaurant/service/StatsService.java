package com.restaurant.service;

import com.restaurant.dto.response.RevenueResponse;
import com.restaurant.dto.response.TopItemResponse;
import com.restaurant.repository.MenuItemRepository;
import com.restaurant.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;

    // Doanh thu theo khoảng ngày
    // Nếu không truyền from/to thì mặc định lấy hôm nay
    public RevenueResponse getRevenue(LocalDate from, LocalDate to) {
        LocalDateTime fromDt = (from != null ? from : LocalDate.now())
                .atStartOfDay();
        LocalDateTime toDt   = (to != null ? to : LocalDate.now())
                .atTime(LocalTime.MAX);

        BigDecimal total = orderRepository.calculateRevenue(fromDt, toDt);

        // Đếm số order completed trong khoảng
        List<Object[]> statusCounts = orderRepository.countByStatusBetween(fromDt, toDt);
        long completedOrders = statusCounts.stream()
                .filter(row -> "COMPLETED".equals(row[0].toString()))
                .mapToLong(row -> (Long) row[1])
                .sum();

        return RevenueResponse.builder()
                .totalRevenue(total)
                .totalOrders(completedOrders)
                .from(fromDt)
                .to(toDt)
                .build();
    }

    // Top món bán chạy
    public List<TopItemResponse> getTopItems() {
        return menuItemRepository.findTopSellingItems().stream()
                .map(row -> TopItemResponse.builder()
                        .menuItemId(((com.restaurant.entity.MenuItem) row[0]).getId())
                        .menuItemName(((com.restaurant.entity.MenuItem) row[0]).getName())
                        .totalSold((Long) row[1])
                        .build())
                .toList();
    }
}
