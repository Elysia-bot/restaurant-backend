package com.restaurant.controller;

import com.restaurant.dto.request.OrderRequest;
import com.restaurant.dto.request.UpdateOrderStatusRequest;
import com.restaurant.dto.response.OrderResponse;
import com.restaurant.enums.OrderStatus;
import com.restaurant.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * POST /api/orders
     * Public (Guest) - Tạo order mới
     *
     * Body:
     * {
     *   "tableId": 3,
     *   "personalNote": "Không cay",
     *   "items": [
     *     { "menuItemId": 1, "quantity": 2 },
     *     { "menuItemId": 5, "quantity": 1 }
     *   ]
     * }
     */
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody OrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(request));
    }

    /**
     * GET /api/orders/table/{tableId}
     * Public (Guest) - Xem tất cả orders của bàn mình
     * Frontend dùng tableId lưu ở localStorage sau khi scan QR
     */
    @GetMapping("/table/{tableId}")
    public ResponseEntity<List<OrderResponse>> getOrdersByTable(
            @PathVariable Long tableId) {
        return ResponseEntity.ok(orderService.getOrdersByTable(tableId));
    }

    /**
     * DELETE /api/orders/{id}
     * Public (Guest) - Huỷ order, chỉ được khi status = PENDING
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/orders
     * Admin only - Xem tất cả orders
     * Query param: status = PENDING | PREPARING | COMPLETED (optional)
     *
     * VD: GET /api/orders?status=PENDING  → chỉ xem orders đang chờ
     * VD: GET /api/orders                 → tất cả
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders(
            @RequestParam(required = false) OrderStatus status) {
        return ResponseEntity.ok(orderService.getAllOrders(status));
    }

    /**
     * PUT /api/orders/{id}/status
     * Admin only - Cập nhật status order
     * Khi update thành công → tự động push WebSocket về bàn của order đó
     *
     * Body: { "status": "PREPARING" }
     * Flow: PENDING → PREPARING → COMPLETED
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(orderService.updateStatus(id, request.getStatus()));
    }
}
