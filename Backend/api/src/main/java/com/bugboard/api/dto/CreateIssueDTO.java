package com.bugboard.api.dto;

import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueType;

public record CreateIssueDTO(
        String uuid,
        String title,
        String description,
        String assignedToUuid,
        String type,
        String priority,
        String imgPath

){}
