package org.frontend.controllers;

import javafx.collections.*;
import org.frontend.models.Issue;
import org.frontend.services.*;

public class DashboardController {

    private final IssueService service;

    private final ObservableList<Issue> issues =
            FXCollections.observableArrayList();

    public DashboardController(){
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

    public ObservableList<Issue> getIssues(){
        return issues;
    }
}