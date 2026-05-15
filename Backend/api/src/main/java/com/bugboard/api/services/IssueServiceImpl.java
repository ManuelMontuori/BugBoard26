package com.bugboard.api.services;

import com.bugboard.api.auth.AuthService;
import com.bugboard.api.dto.CreateIssueDTO;
import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.mapper.IssueMapper;

import com.bugboard.api.models.*;
import com.bugboard.api.repositories.IssueRepository;
import com.bugboard.api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final AuthService authService;
    private final IssueMapper issueMapper;
    private final UserRepository userRepository;

    public IssueServiceImpl(IssueRepository issueRepository,
                            AuthService authService,
                            IssueMapper issueMapper,
                            UserRepository userRepository) {
        this.issueRepository = issueRepository;
        this.authService = authService;
        this.issueMapper = issueMapper;
        this.userRepository = userRepository;
    }

    @Override
    public IssueDTO createIssue(CreateIssueDTO dto) {
        if (dto.title() == null || dto.title().isBlank()) {
            throw new IllegalArgumentException("Title is required");
        }

        if (dto.description() == null || dto.description().isBlank()) {
            throw new IllegalArgumentException("Description is required");
        }

        User reporter = authService.getCurrentUser().orElseThrow(() -> new IllegalArgumentException("User not authenticated"));

        Issue issue = new Issue();
        issue = issueMapper.mapToEntity(dto, issue);
        issue.setStatus(IssueStatus.TODO);
        issue.setReporter(reporter);


        if (dto.assignedToUuid() != null) {
            User assignedTo = userRepository.findByUuid(
                    UUID.fromString(dto.assignedToUuid())).orElseThrow(
                    () -> new IllegalArgumentException("Assigned user not found"));
            issue.setAssignedTo(assignedTo);
        }
            Issue saved= issueRepository .save(issue);

            return issueMapper.mapToDTO(saved);
    }

    @Override
    public List<IssueDTO> getIssues(Order view, IssueStatus status, IssuePriority priority, IssueType type) {
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


        if(view!=null){
            switch(view) {
                case STATUS_ASC:
                    issues.sort(Comparator.comparing(Issue::getStatus));
                    break;
                case STATUS_DESC:
                    issues.sort(Comparator.comparing(Issue::getStatus).reversed());
                    break;
                case PRIORITY_ASC:
                    issues.sort(Comparator.comparing(Issue::getPriority));
                    break;
                case PRIORITY_DESC:
                    issues.sort(Comparator.comparing(Issue::getPriority).reversed());
                    break;
                case  TYPE_ASC:
                    issues.sort(Comparator.comparing(Issue::getType));
                    break;
                case TYPE_DESC:
                    issues.sort(Comparator.comparing(Issue::getType).reversed());
                    break;
                case DATE_ASC:
                    issues.sort(Comparator.comparing(Issue::getCreatedAt));
                    break;
                case DATE_DESC:
                    issues.sort(Comparator.comparing(Issue::getCreatedAt).reversed());
                    break;
            }
        }

        return issues.stream()
                .map(issueMapper::mapToDTO)
                .toList();
    }

    @Override
    public IssueDTO getIssueByUuid(UUID id) {
        return null;
    }

    @Override
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

}
