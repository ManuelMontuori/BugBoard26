package com.bugboard.api.mapper;

import org.springframework.stereotype.Component;

import com.bugboard.api.dto.UserReportDTO;

@Component
public class UserReportMapper {

    public UserReportDTO mapToDTO(UserReportProjection report){
        return new UserReportDTO(
                report.getUuid().toString(),
                report.getFirstName(),
                report.getLastName(),
                report.getEmail(),
                report.getTotIssue(),
                report.getTotCreatedIssue(),
                report.getTotResolvedIssue(),
                report.getTotWorkloadIssue(),
                report.getAverageResolutionTime(),
                report.getTotHightPriorityIssue()

        )
    }

}
