package com.restaurant.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class FeedbackRequest {

    @NotNull(message = "Phải có tableId")
    private Long tableId;

    @NotNull
    @Min(value = 1, message = "Rating thấp nhất là 1 sao")
    @Max(value = 5, message = "Rating cao nhất là 5 sao")
    private Integer rating;

    private String comment;
}
