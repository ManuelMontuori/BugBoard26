package com.bugboard.api.dto;

import com.bugboard.api.models.User;

public record WorkloadDTO(
        User user,
        Long issuesCount
) {}
