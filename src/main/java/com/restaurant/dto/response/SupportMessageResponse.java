package com.restaurant.dto.response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class SupportMessageResponse {
    private Long id;
    private Long tableId;
    private Integer tableNumber;
    private String message;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
