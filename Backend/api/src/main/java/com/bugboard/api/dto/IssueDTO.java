package com.bugboard.api.dto;

import java.time.LocalDateTime;

public record IssueDTO(
        String uuid,
        String title,
        String description,
        String type,
        String priority,
        String status,
        String reporterUuid,
        String assignedToUuid,
        String imgPath,
        LocalDateTime createdAt,
        LocalDateTime resolvedAt,
        LocalDateTime assignedAt
) {
}
