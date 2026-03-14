package com.restaurant.dto.request;

import com.restaurant.enums.OrderStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

// Admin cập nhật status order
@Data
public class UpdateOrderStatusRequest {

    @NotNull(message = "Status không được để trống")
    private OrderStatus status;
}
