package com.bugboard.api.repositories.projection;

public interface UserReportProjection {
    String getUserUuid();
    String getFirstName();
    String getLastName();
    String getEmail();
    Integer getTotIssues();
    Integer getTotCreatedIssues();
    Integer getTotResolvedIssues();
    Integer getTotWorkloadIssues();
    Double getAverageIssues();
    Integer getTotHighPriorityIssues();
}
