package org.frontend.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.frontend.models.Issue;
import org.frontend.services.IssueService;
import org.frontend.util.BackendServiceUtil;

public class IssueController {

    private final IssueService service;
    private final ObservableList<Issue> issues;

    public IssueController() {
        this.service = BackendServiceUtil.getInstance().getIssueService();
        this.issues = FXCollections.observableArrayList();
    }

    public ObservableList<Issue> getIssues() {
        return issues;
    }

    // Metodo con i filtri
    public void loadAllIssues(String status, String priority, String type) {
        issues.setAll(service.findAll(status, priority, type));
    }

    // Metodo di overload per caricare tutto senza filtri
    public void loadAllIssues() {
        issues.setAll(service.findAll(null, null, null));
    }

    public void loadMyIssues(String uuid) {
        issues.setAll(service.findAssignedToMe(uuid));
    }

    public void createIssue(String title, String description, String type, String priority) throws Exception{
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