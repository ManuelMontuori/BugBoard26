package com.bugboard.api.dto;



public record UserReportDTO (

    String uuid,
    String firstName,
    String lastName,
    String email,
    Integer totIssue,
    Integer totCreatedIssue,
    Integer totResolvedIssue,
    Integer totWorkloadIssue,
    Integer averageResolutionTime,
    Integer totHightPriorityIssue   
){}
    

