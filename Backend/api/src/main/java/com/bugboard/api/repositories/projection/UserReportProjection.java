package com.bugboard.api.repositories.projection;

public interface UserReportProjection {
    String getUuid();
    String getFirstName();
    String getLastName();
    String getEmail();
    Integer getTotIssue();
    Integer getTotCreatedIssue();
    Integer getTotResolvedIssue();
    Integer getTotWorkloadIssue();
    Double getAverageIssue();
    Integer getTotHighPriorityIssue();
}
