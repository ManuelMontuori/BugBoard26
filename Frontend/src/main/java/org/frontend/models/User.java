package org.frontend.models;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record User(
        String firstName,
        String lastName,
        String uuid,
        String email,
        String role,
        String status,
        LocalDate lastLogin,
        LocalDateTime createdAt
) {}
