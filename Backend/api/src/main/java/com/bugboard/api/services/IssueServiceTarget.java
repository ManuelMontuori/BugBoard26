package com.bugboard.api.services;

import com.bugboard.api.models.Issue;
import com.bugboard.api.models.IssuePriority;
import com.bugboard.api.models.IssueStatus;
import com.bugboard.api.models.IssueType;

import java.util.List;

public interface IssueServiceTarget {
    List<Issue> findByStatus(IssueStatus status);

    List<Issue> findByPriority(IssuePriority priority);

    List<Issue> findByType(IssueType type);

    List<Issue> findByStatusAndPriority(IssueStatus status, IssuePriority priority);

    List<Issue> findByStatusAndType(IssueStatus status, IssueType type);

    List<Issue> findByPriorityAndType(IssuePriority priority, IssueType type);

    List<Issue> findByStatusAndPriorityAndType(IssueStatus status,
                                               IssuePriority priority,
                                               IssueType type);

    List<Issue> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String titleKeyword,
                                                                                 String descriptionKeyword);

    List<Issue> findAll();
    Issue save(Issue issue);
}
