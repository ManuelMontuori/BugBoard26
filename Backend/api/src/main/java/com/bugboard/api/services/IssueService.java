package com.bugboard.api.services;

import com.bugboard.api.dto.CreateIssueDTO;
import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.dto.UserDTO;
import com.bugboard.api.models.*;

import java.util.List;
import java.util.UUID;

public interface IssueService {
    IssueDTO createIssue(CreateIssueDTO dto);

    List<IssueDTO> getIssues(Order view, IssueStatus status, IssuePriority priority, IssueType type);

    IssueDTO getIssueByUuid(UUID id);

    List<IssueDTO> searchIssueByTitleOrDescription(String keyword);

    List<IssueDTO> findByAssignedToUuid(UUID assignedTo);
}
