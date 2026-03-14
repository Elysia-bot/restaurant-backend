package com.restaurant.dto.response;
import lombok.Builder;
import lombok.Data;

@Data @Builder
public class TopItemResponse {
    private Long menuItemId;
    private String menuItemName;
    private Long totalSold;
}
