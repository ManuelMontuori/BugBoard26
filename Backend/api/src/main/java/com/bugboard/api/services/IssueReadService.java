package com.bugboard.api.services;

import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.mapper.IssueMapper;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.IssueType;
import com.bugboard.api.repositories.IssueRepository;
import com.bugboard.api.specification.IssueSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

        Specification<Issue> spec = Specification
                .where(IssueSpecification.hasStatus(status))
                .and(IssueSpecification.hasPriority(priority))
                .and(IssueSpecification.hasType(type));

        List<Issue> issues = issueRepository.findAll(spec);

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

    public Optional<IssueDTO> getIssueByUuid(UUID uuid) {
        return Optional.ofNullable(issueMapper.mapToDTO(issueRepository.findByUuid(uuid).orElseThrow(() ->
                new RuntimeException("Issue non presente"))));
    }

}
