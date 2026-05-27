package com.bugboard.api.services;

import com.bugboard.api.auth.AuthService;
import com.bugboard.api.dto.CreateIssueDTO;
import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.mapper.IssueMapper;

import com.bugboard.api.models.*;
import com.bugboard.api.observer.Observer;
import com.bugboard.api.repositories.IssueRepository;
import com.bugboard.api.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final IssueMapper issueMapper;

    public IssueServiceImpl(IssueRepository issueRepository,
                            UserRepository userRepository,
                            AuthService authService,
                            IssueMapper issueMapper) {
        this.issueRepository = issueRepository;
        this.userRepository=userRepository;
        this.authService = authService;
        this.issueMapper = issueMapper;
        
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
            Issue saved= issueRepository.save(issue);

            return issueMapper.mapToDTO(saved);
    }

    @Override
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

    @Override
    public List<IssueDTO> findByAssignedToUuid(UUID assignedTo) {
        return issueRepository.findByAssignedToUuid(assignedTo)
                .stream()
                .map(issueMapper::mapToDTO)
                .toList();
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Issue issue, User user) {
        for (Observer observer : observers) {
            observer.update(issue, user);
        }
    }

    @Override
    public void assignIssue(UUID issueUuid, UUID userUuid) {
        Issue issue = issueRepository.findByUuid(issueUuid);
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        issue.setAssignedTo(user);
        issue.setAssigned_at(LocalDateTime.now());
//        User reporter=authService.getCurrentUser();\
        notifyObservers(issue, user); // aggiungi anche reporter
    }

}
