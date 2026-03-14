package com.restaurant.dto.response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data @Builder
public class FeedbackResponse {
    private Long id;
    private Long tableId;
    private Integer tableNumber;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}
