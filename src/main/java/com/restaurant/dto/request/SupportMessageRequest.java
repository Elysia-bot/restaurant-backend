package com.restaurant.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SupportMessageRequest {

    @NotNull(message = "Phải có tableId")
    private Long tableId;

    @NotBlank(message = "Tin nhắn không được để trống")
    private String message;
}
