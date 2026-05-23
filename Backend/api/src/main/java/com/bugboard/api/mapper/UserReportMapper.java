package com.bugboard.api.mapper;

import org.springframework.stereotype.Component;

import com.bugboard.api.dto.UserReportDTO;
import com.bugboard.api.repositories.projection.UserReportProjection;

@Component
public class UserReportMapper {

    public UserReportDTO mapToDTO(UserReportProjection report) {
        Double avg = report.getAverageIssues() != null
                ? report.getAverageIssues().doubleValue()
                : null;

        return new UserReportDTO(
                report.getUuid(),
                report.getFirstName(),
                report.getLastName(),
                report.getEmail(),
                report.getTotIssues(),
                report.getTotCreatedIssues(),
                report.getTotResolvedIssues(),
                report.getTotWorkloadIssues(),
                avg,
                report.getTotHighPriorityIssues()
        );
    }
}


