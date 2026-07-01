package com.bugboard.api.repositories;

import com.bugboard.api.models.Issue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IssueRepository extends JpaRepository<Issue, Long>, JpaSpecificationExecutor<Issue> {

    List<Issue> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword,
            String descriptionKeyword
    );

    List<Issue> findByAssignedToUuid(UUID assignedTo);

    Optional<Issue> findByUuid(UUID uuid);

}
