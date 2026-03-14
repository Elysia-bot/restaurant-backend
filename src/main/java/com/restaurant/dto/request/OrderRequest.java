package com.restaurant.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {

    @NotNull(message = "Phải có tableId")
    private Long tableId;

    private String personalNote;

    @NotEmpty(message = "Order phải có ít nhất 1 món")
    private List<OrderItemRequest> items;

    @Data
    public static class OrderItemRequest {

        @NotNull(message = "Phải có menuItemId")
        private Long menuItemId;

        @NotNull
        @Min(value = 1, message = "Số lượng phải >= 1")
        private Integer quantity;
    }
}
