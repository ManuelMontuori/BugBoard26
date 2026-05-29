package com.bugboard.api.services;

import com.bugboard.api.auth.AuthService;
import com.bugboard.api.dto.CreateIssueDTO;
import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.mapper.IssueMapper;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.User;
import com.bugboard.api.repositories.IssueRepository;
import com.bugboard.api.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class IssueWriteService {
    private final IssueRepository issueRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final IssueMapper issueMapper;
    private final IssueObserverComponent issueObserverService;

    public IssueWriteService(IssueRepository issueRepository,
                             UserRepository userRepository,
                             AuthService authService,
                             IssueMapper issueMapper,
                             IssueObserverComponent issueObserverService
    ) {
        this.issueRepository = issueRepository;
        this.userRepository = userRepository;
        this.authService = authService;
        this.issueMapper = issueMapper;
        this.issueObserverService = issueObserverService;
    }

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

    public void assignIssue(UUID issueUuid, UUID userUuid) {
        Issue issue = issueRepository.findByUuid(issueUuid);
        User user = userRepository.findByUuid(userUuid)
                .orElseThrow(()->new IllegalArgumentException("User not found"));
        issue.setAssignedTo(user);
        issue.setAssigned_at(LocalDateTime.now());
//        User reporter=authService.getCurrentUser();\
        issueObserverService.IssueObserverComponent(issue, user); // aggiungi anche reporter
    }
}
