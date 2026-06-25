package org.frontend.models.dtos;

public record UserWorkloadDTO(
        String uuid,
        String firstName,
        String lastName,
        Long   issuesCount
) {}