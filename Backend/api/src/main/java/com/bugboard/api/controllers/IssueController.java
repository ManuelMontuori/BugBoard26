package com.bugboard.api.controllers;

import com.bugboard.api.dto.CreateIssueDTO;
import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.IssueType;
import com.bugboard.api.observer.NotificationObserver;
import com.bugboard.api.services.IssueObserverComponent;
import com.bugboard.api.services.IssueService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/issues")
public class IssueController {
    private final IssueService issueService;
    private final IssueObserverComponent issueObserverComponent;

    public IssueController(IssueService issueService, IssueObserverComponent issueObserverComponent){
        this.issueService = issueService;
        this.issueObserverComponent = issueObserverComponent;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueDTO createIssue(@RequestBody CreateIssueDTO dto) {
        return issueService.createIssue(dto);
    }

    @GetMapping
    public List<IssueDTO> getIssues(@RequestParam(required = false) IssueStatus status,
                                    @RequestParam(required = false) IssuePriority priority,
                                    @RequestParam(required = false) IssueType type) {
        return issueService.getIssues(status, priority, type);
    }

    @GetMapping("/search")
    public List<IssueDTO> searchIssues(@RequestParam String keyword) {
        return issueService.searchIssueByTitleOrDescription(keyword);
    }

    @GetMapping("/assigned")
    public List<IssueDTO> getAssignedIssues(@RequestParam UUID assignedTo) {
        return issueService.findByAssignedToUuid(assignedTo);
    }

    @PatchMapping("/assigned")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignIssue(@RequestParam UUID issueUuid, @RequestParam UUID userUuid) {
        issueObserverComponent.attach(new NotificationObserver());
        issueService.assignIssue(issueUuid, userUuid);
    }


}
