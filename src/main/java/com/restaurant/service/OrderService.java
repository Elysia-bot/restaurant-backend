package com.restaurant.service;

import com.restaurant.dto.request.OrderRequest;
import com.restaurant.dto.response.OrderItemResponse;
import com.restaurant.dto.response.OrderResponse;
import com.restaurant.dto.response.OrderStatusUpdate;
import com.restaurant.entity.*;
import com.restaurant.enums.OrderStatus;
import com.restaurant.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestaurantTableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;

    // WebSocket: dùng để push status update xuống client
    private final SimpMessagingTemplate messagingTemplate;

    // ─── Guest: Tạo order mới ────────────────────────────────────────────────

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bàn"));

        // Tạo order trước
        Order order = Order.builder()
                .table(table)
                .personalNote(request.getPersonalNote())
                .status(OrderStatus.PENDING)
                .build();




        // Tạo các order items
        List<OrderItem> orderItems = request.getItems().stream().map(itemReq -> {
            MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                    .orElseThrow(() -> new RuntimeException(
                            "Không tìm thấy món ăn id=" + itemReq.getMenuItemId()));

            if (!menuItem.getIsAvailable()) {
                throw new RuntimeException("Món '" + menuItem.getName() + "' tạm thời hết hàng");
            }

            BigDecimal unitPrice = menuItem.getPrice();
            BigDecimal subTotal  = unitPrice.multiply(BigDecimal.valueOf(itemReq.getQuantity()));

            return OrderItem.builder()
                    .order(order)/// /////////
                    .menuItem(menuItem)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(unitPrice)       // snapshot giá
                    .subTotal(subTotal)
                    .build();
        }).toList();

        order.setOrderItems(orderItems);



        // Tính tổng tiền
        BigDecimal totalPrice = orderItems.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(totalPrice);

        Order saved = orderRepository.save(order);
        return toResponse(saved);
    }

    // ─── Guest: Xem tất cả orders của bàn mình ───────────────────────────────

    public List<OrderResponse> getOrdersByTable(Long tableId) {
        return orderRepository.findByTableIdOrderByCreatedAtDesc(tableId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // ─── Guest: Huỷ order (chỉ khi PENDING) ─────────────────────────────────

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy order"));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể huỷ order đang ở trạng thái Pending");
        }

        orderRepository.delete(order);
    }

    // ─── Admin: Xem tất cả orders ────────────────────────────────────────────

    public List<OrderResponse> getAllOrders(OrderStatus status) {
        if (status != null) {
            return orderRepository.findByStatusOrderByCreatedAtAsc(status)
                    .stream().map(this::toResponse).toList();
        }
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    // ─── Admin: Cập nhật status → push WebSocket ─────────────────────────────

    @Transactional
    public OrderResponse updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy order"));

        validateStatusTransition(order.getStatus(), newStatus);

        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);

        // Push realtime về bàn của order này
        // Client subscribe: /topic/orders/{tableId}
        messagingTemplate.convertAndSend(
                "/topic/orders/" + saved.getTable().getId(),
                OrderStatusUpdate.builder()
                        .orderId(saved.getId())
                        .tableId(saved.getTable().getId())
                        .status(saved.getStatus())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        return toResponse(saved);
    }

    // Chỉ cho phép đi đúng chiều: PENDING → PREPARING → COMPLETED
    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        boolean valid = switch (current) {
            case PENDING   -> next == OrderStatus.PREPARING;
            case PREPARING -> next == OrderStatus.COMPLETED;
            case COMPLETED -> false;
        };
        if (!valid) {
            throw new RuntimeException(
                    "Không thể chuyển từ " + current + " sang " + next);
        }
    }

    // ─── Mapper ───────────────────────────────────────────────────────────────

    private OrderResponse toResponse(Order o) {
        List<OrderItemResponse> items = o.getOrderItems().stream()
                .map(oi -> OrderItemResponse.builder()
                        .id(oi.getId())
                        .menuItemId(oi.getMenuItem().getId())
                        .menuItemName(oi.getMenuItem().getName())
                        .quantity(oi.getQuantity())
                        .unitPrice(oi.getUnitPrice())
                        .subTotal(oi.getSubTotal())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(o.getId())
                .tableId(o.getTable().getId())
                .tableNumber(o.getTable().getTableNumber())
                .status(o.getStatus())
                .personalNote(o.getPersonalNote())
                .totalPrice(o.getTotalPrice())
                .createdAt(o.getCreatedAt())
                .updatedAt(o.getUpdatedAt())
                .items(items)
                .build();
    }
}
