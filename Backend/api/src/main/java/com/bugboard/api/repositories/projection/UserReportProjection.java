package com.bugboard.api.repositories.projection;

import java.math.BigDecimal;

public interface UserReportProjection {
    String getUuid();
    String getFirstName();
    String getLastName();
    String getEmail();
    //Ho messo al plurale Issues e cambiato double in BigDecimal
    Integer getTotIssues();
    Integer getTotCreatedIssues();
    Integer getTotResolvedIssues();
    Integer getTotWorkloadIssues();
    BigDecimal getAverageIssues();
    Integer getTotHighPriorityIssues();

    

   

    
    
}
