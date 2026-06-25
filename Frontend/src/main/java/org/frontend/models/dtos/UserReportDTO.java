package org.frontend.models.dtos;

public record UserReportDTO(
        String uuid,
        String firstName,
        String lastName,
        String email,
        Integer totIssue,
        Integer totCreatedIssue,
        Integer totResolvedIssue,
        Integer totWorkloadIssue,
        Double averageResolutionTime,
        Integer totHightPriorityIssue
){}