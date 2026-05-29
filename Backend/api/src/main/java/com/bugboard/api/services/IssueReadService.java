package com.bugboard.api.services;

import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.mapper.IssueMapper;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.IssueType;
import com.bugboard.api.repositories.IssueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class IssueReadService {
    private final IssueRepository issueRepository;
    private final IssueMapper issueMapper;

    public IssueReadService(IssueRepository issueRepository, IssueMapper issueMapper) {
        this.issueRepository = issueRepository;
        this.issueMapper = issueMapper;
    }

    public List<IssueDTO> getIssues(IssueStatus status, IssuePriority priority, IssueType type) {
        List<Issue> issues;

        if (status != null && priority != null && type != null) {
            issues = issueRepository
                    .findByStatusAndPriorityAndType(status, priority, type);

        } else if (status != null && priority != null) {
            issues = issueRepository
                    .findByStatusAndPriority(status, priority);

        } else if (status != null && type != null) {
            issues = issueRepository
                    .findByStatusAndType(status, type);

        } else if (priority != null && type != null) {
            issues = issueRepository
                    .findByPriorityAndType(priority, type);

        } else if (status != null) {
            issues = issueRepository.findByStatus(status);

        } else if (priority != null) {
            issues = issueRepository.findByPriority(priority);

        } else if (type != null) {
            issues = issueRepository.findByType(type);

        } else {
            issues = issueRepository.findAll();
        }

        return issues.stream()
                .map(issueMapper::mapToDTO)
                .toList();
    }

    public List<IssueDTO> searchIssueByTitleOrDescription(String keyword) {
        if(keyword==null || keyword.isBlank()) {
            throw new IllegalArgumentException("Keyword is required");
        }

        List<Issue> issues = issueRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);

        return issues.stream()
                .map(issueMapper::mapToDTO)
                .toList();
    }

    public List<IssueDTO> findByAssignedToUuid(UUID assignedTo) {
        return issueRepository.findByAssignedToUuid(assignedTo)
                .stream()
                .map(issueMapper::mapToDTO)
                .toList();
    }

    public IssueDTO getIssueByUuid(UUID uuid) {
        return issueMapper.mapToDTO(issueRepository.findByUuid(uuid));
    }

}
