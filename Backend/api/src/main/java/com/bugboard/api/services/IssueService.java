package com.bugboard.api.services;

import com.bugboard.api.dto.CreateIssueDTO;
import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.dto.UserDTO;
import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.IssueType;
import com.bugboard.api.models.User;

import java.util.List;
import java.util.UUID;

public interface IssueService {
    IssueDTO createIssue(CreateIssueDTO dto);

    List<IssueDTO> getIssues(IssueStatus status, IssuePriority priority, IssueType type);

    IssueDTO getIssueByUuid(UUID id);

    List<IssueDTO> searchIssueByTitleOrDescription(String keyword);

}
