package com.bugboard.api.dto;

import java.time.LocalDateTime;

public record NotificationDTO(
        String uuid,
        String message,
        boolean read,
        LocalDateTime createdAt)
{}
