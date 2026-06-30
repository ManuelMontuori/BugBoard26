package com.bugboard.api.dto;

public record CreateIssueDTO(
        String uuid,
        String title,
        String description,
        String assignedToUuid,
        String type,
        String priority,
        String imgPath
){}
