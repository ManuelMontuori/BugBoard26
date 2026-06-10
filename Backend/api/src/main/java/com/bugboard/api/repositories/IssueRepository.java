package com.bugboard.api.repositories;

import com.bugboard.api.models.Issue;
import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.IssueType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue> {

    List<Issue> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword,
            String descriptionKeyword
    );

    List<Issue> findByAssignedToUuid(UUID assignedTo);

    Issue findByUuid(UUID uuid);

}
