package com.bugboard.api.repositories;

import com.bugboard.api.models.Issue;
import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.IssueType;
import com.bugboard.api.services.IssueServiceTarget;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class IssueRepositoryAdapter implements IssueServiceTarget {

    private final IssueRepositoryAdaptee issueRepositoryAdaptee;
    public IssueRepositoryAdapter(IssueRepositoryAdaptee issueRepositoryAdaptee) {
        this.issueRepositoryAdaptee = issueRepositoryAdaptee;
    }

    @Override
    public List<Issue> findByStatus(IssueStatus status) {
        return issueRepositoryAdaptee.findByStatus(status);
    }

    @Override
    public List<Issue> findByPriority(IssuePriority priority) {
        return issueRepositoryAdaptee.findByPriority(priority);
    }

    @Override
    public List<Issue> findByType(IssueType type) {
        return issueRepositoryAdaptee.findByType(type);
    }

    @Override
    public List<Issue> findByStatusAndPriority(IssueStatus status, IssuePriority priority) {
        return issueRepositoryAdaptee.findByStatusAndPriority(status, priority);
    }

    @Override
    public List<Issue> findByStatusAndType(IssueStatus status, IssueType type) {
        return issueRepositoryAdaptee.findByStatusAndType(status, type);
    }

    @Override
    public List<Issue> findByPriorityAndType(IssuePriority priority, IssueType type) {
        return issueRepositoryAdaptee.findByPriorityAndType(priority, type);
    }

    @Override
    public List<Issue> findByStatusAndPriorityAndType(IssueStatus status,
                                                      IssuePriority priority,
                                                      IssueType type) {
        return issueRepositoryAdaptee.findByStatusAndPriorityAndType(status, priority, type);
    }

    @Override
    public List<Issue> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String titleKeyword,
            String descriptionKeyword) {
        return issueRepositoryAdaptee.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(titleKeyword, descriptionKeyword);
    }

    @Override
    public List<Issue> findAll() {
        return issueRepositoryAdaptee.findAll();
    }

    @Override
    public Issue save(Issue issue) {
        return issueRepositoryAdaptee.save(issue);
    }

    @Override
    public List<Issue> findByAssignedToUuid(UUID assignedTo) {
        return issueRepositoryAdaptee.findByAssignedToUuid(assignedTo);
    }

}
