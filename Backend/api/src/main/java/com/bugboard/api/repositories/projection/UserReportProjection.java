package com.bugboard.api.repositories.projection;

import java.math.BigDecimal;

public interface UserReportProjection {
    String getUuid();
    String getFirstName();
    String getLastName();
    String getEmail();
    Integer getTotIssues();
    Integer getTotCreatedIssues();
    Integer getTotResolvedIssues();
    Integer getTotWorkloadIssues();
    BigDecimal getAverageIssues();
    Integer getTotHighPriorityIssues();

}
