package com.bugboard.api.services;

import com.bugboard.api.dto.CreateIssueDTO;
import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.models.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IssueService {

    

    IssueDTO createIssue(CreateIssueDTO dto);

    List<IssueDTO> getIssues(IssueStatus status, IssuePriority priority, IssueType type);

    Optional<IssueDTO> getIssueByUuid(UUID id);

    List<IssueDTO> searchIssueByTitleOrDescription(String keyword);

    List<IssueDTO> findByAssignedToUuid(UUID assignedTo);

    void assignIssue(UUID issueUuid, UUID userUuid);
}
