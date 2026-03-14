package com.restaurant.dto.response;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class TableResponse {
    private Long id;
    private Integer tableNumber;
    private String qrCode;
    private Boolean isOccupied;
}
