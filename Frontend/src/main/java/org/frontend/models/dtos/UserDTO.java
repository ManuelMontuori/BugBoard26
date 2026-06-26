package org.frontend.models.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record UserDTO(
        String uuid,
        String email,
        String role,
        String firstName,
        String lastName,
        String status,
        LocalDate lastLogin,
        LocalDateTime createdAt
) {}