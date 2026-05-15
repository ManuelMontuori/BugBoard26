package com.bugboard.api.controllers;

import com.bugboard.api.dto.CreateIssueDTO;
import com.bugboard.api.dto.IssueDTO;
import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.IssueType;
import com.bugboard.api.models.SummaryView;
import com.bugboard.api.services.IssueService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/issues")
public class IssueController {
    private final IssueService issueService;

    public IssueController(IssueService issueService) {
        this.issueService = issueService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IssueDTO createIssue(@RequestBody CreateIssueDTO dto) {
        return issueService.createIssue(dto);
    }

    @GetMapping
    public List<IssueDTO> getIssues(@RequestParam(required = false) SummaryView view,
                                    @RequestParam(required = false) IssueStatus status,
                                    @RequestParam(required = false) IssuePriority priority,
                                    @RequestParam(required = false) IssueType type) {
        return issueService.getIssues(view, status, priority, type);
    }

    @GetMapping("/search")
    public List<IssueDTO> searchIssues(@RequestParam String keyword) {
        return issueService.searchIssueByTitleOrDescription(keyword);
    }
}
