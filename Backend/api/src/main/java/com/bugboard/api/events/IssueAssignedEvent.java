package com.bugboard.api.events;

import com.bugboard.api.models.Issue;
import com.bugboard.api.models.User;

public class IssueAssignedEvent {

    private final Issue issue;
    private final User user;

    public IssueAssignedEvent(Issue issue, User user) {
        this.issue = issue;
        this.user = user;
    }

    public Issue getIssue() {
        return issue;
    }

    public User getUser() {
        return user;
    }
}




