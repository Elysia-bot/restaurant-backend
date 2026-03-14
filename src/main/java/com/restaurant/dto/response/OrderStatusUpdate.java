package com.restaurant.dto.response;
import com.restaurant.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class OrderStatusUpdate {
    private Long orderId;
    private Long tableId;
    private OrderStatus status;
    private LocalDateTime updatedAt;
}
