package com.restaurant.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// ─── Auth ────────────────────────────────────────────────────────────────────

@Data
public class LoginRequest {
    @NotBlank(message = "Username không được để trống")
    private String username;

    @NotBlank(message = "Password không được để trống")
    private String password;
}
