package com.bugboard.api.dto;

public record WorkloadDTO(
        UserDTO user,
        Long issuesCount
) { }
