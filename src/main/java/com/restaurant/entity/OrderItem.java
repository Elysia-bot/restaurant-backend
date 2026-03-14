package com.restaurant.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private MenuItem menuItem;

    @Column(nullable = false)
    private Integer quantity;

    // Snapshot giá tại thời điểm order — quan trọng!
    // Admin có thể đổi giá sau, nhưng order cũ vẫn giữ giá gốc
    @Column(nullable = false, precision = 10, scale = 0)
    private BigDecimal unitPrice;

    // unitPrice * quantity
    @Column(nullable = false, precision = 10, scale = 0)
    private BigDecimal subTotal;
}
