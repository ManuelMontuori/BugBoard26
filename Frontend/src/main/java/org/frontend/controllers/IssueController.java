package org.frontend.controllers;

import javafx.collections.*;
import org.frontend.models.Issue;
import org.frontend.services.*;

public class IssueController {

    private final IssueService service;

    private final ObservableList<Issue> issues =
            FXCollections.observableArrayList();

    public IssueController(){
        ApiClient client =
                new ApiClient(
                        "http://localhost:8080"
                );

        IssueApiService api =
                new IssueApiService(client);

        service =
                new IssueService(api);

    }

    public void loadIssues(){

        issues.setAll(
                service.findAll()
        );

    }

    public void loadMyIssues(String uuid){
        issues.setAll(
                service.findAssignedToMe(uuid)
        );

    }

    public void createIssue(String title, String description,
                            String type, String priority) {
        Issue issue = new Issue();
        issue.setTitle(title);
        issue.setDescription(description);
        issue.setType(type);
        issue.setPriority(priority);

        service.createIssue(issue);
    }

    public ObservableList<Issue> getIssues(){
        return issues;
    }
}