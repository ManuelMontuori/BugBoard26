package com.bugboard.api.repositories;

import com.bugboard.api.dto.UserReportDTO;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.IssueType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface IssueRepositoryAdaptee extends JpaRepository<Issue, Long> {
    // soliti metodi CRUD sono già disponibili grazie a JpaRepository

    List<Issue> findByStatus(IssueStatus status);

    List<Issue> findByPriority(IssuePriority priority);

    List<Issue> findByType(IssueType type);

    List<Issue> findByStatusAndPriority(IssueStatus status, IssuePriority priority);

    List<Issue> findByStatusAndType(IssueStatus status, IssueType type);

    List<Issue> findByPriorityAndType(IssuePriority priority, IssueType type);

    List<Issue> findByStatusAndPriorityAndType(
            IssueStatus status,
            IssuePriority priority,
            IssueType type
    );

    List<Issue> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword,
            String descriptionKeyword
    );

    List<Issue> findByAssignedToUuid(UUID assignedTo);

    Issue findByUuid(UUID uuid);

}
