package org.frontend.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.frontend.models.Issue;
import org.frontend.services.IssueService;
import org.frontend.util.BackendServiceFactory;

public class IssueController {

    private final IssueService service;
    private final ObservableList<Issue> issues;

    // Costruttore ora è PUBBLICO
    public IssueController() {
        this.service = BackendServiceFactory.getInstance().getIssueService();
        this.issues = FXCollections.observableArrayList();
    }

    public ObservableList<Issue> getIssues() {
        return issues;
    }

    public void loadAllIssues() {
        issues.setAll(service.findAll());
    }

    public void loadMyIssues(String uuid) {
        issues.setAll(service.findAssignedToMe(uuid));
    }

    public void createIssue(String title, String description, String type, String priority) {
        Issue issue = new Issue();
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setType(type);
        issue.setPriority(priority);

        service.createIssue(issue);
    }

    public void searchIssue(String keyword) {
        issues.setAll(service.searchIssue(keyword));
    }
}