package com.bugboard.api.services;

import com.bugboard.api.dto.CreateIssueDTO;
import com.bugboard.api.dto.IssueDTO;

import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.IssueType;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueWriteService issueWriteService;
    private final IssueReadService issueReadService;

    public IssueServiceImpl(IssueWriteService issueWriteService,
            IssueReadService issueReadService) {
        this.issueWriteService = issueWriteService;
        this.issueReadService = issueReadService;
    }

    @Override
    public IssueDTO createIssue(CreateIssueDTO dto) {
        return issueWriteService.createIssue(dto);
    }

    @Override
    public List<IssueDTO> getIssues(IssueStatus status, IssuePriority priority, IssueType type) {
        return issueReadService.getIssues(status, priority, type);
    }

    @Override
    public Optional<IssueDTO> getIssueByUuid(UUID uuid) {
        return issueReadService.getIssueByUuid(uuid);
    }

    @Override
    public List<IssueDTO> searchIssueByTitleOrDescription(String keyword) {
        return issueReadService.searchIssueByTitleOrDescription(keyword);
    }

    @Override
    public List<IssueDTO> findByAssignedToUuid(UUID assignedTo) {
        return issueReadService.findByAssignedToUuid(assignedTo);
    }

    @Override
    public void assignIssue(UUID issueUuid, UUID userUuid) {
        issueWriteService.assignIssue(issueUuid, userUuid);
    }

}
