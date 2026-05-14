package com.bugboard.api.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDTO(
        String uuid,
        String email,
        String role,
        String status,
        LocalDate lastLogin,
        LocalDateTime createdAt
) {
}
