package com.restaurant.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "restaurant_tables")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Số bàn in trên menu giấy: 1, 2, 3...
    @Column(nullable = false, unique = true)
    private Integer tableNumber;

    // QR hardcode theo số bàn, VD: "TABLE_1", "TABLE_2"
    // QR code vật lý chứa URL: http://localhost:3000/scan?qr=TABLE_1
    @Column(nullable = false, unique = true)
    private String qrCode;

    // true = đang có khách, false = bàn trống
    @Column(nullable = false)
    @Builder.Default
    private Boolean isOccupied = false;

    // Một bàn có thể có nhiều order (khách gọi nhiều lần)
    @JsonIgnore
    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<Order> orders;

    @JsonIgnore
    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<Feedback> feedbacks;

    @JsonIgnore
    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
    private List<SupportMessage> supportMessages;
}
