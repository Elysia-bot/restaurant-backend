package com.restaurant.dto.response;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data @Builder
public class RevenueResponse {
    private BigDecimal totalRevenue;
    private Long totalOrders;
    private LocalDateTime from;
    private LocalDateTime to;
}
