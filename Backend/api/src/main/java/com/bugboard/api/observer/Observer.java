package com.bugboard.api.observer;

import com.bugboard.api.models.Issue;
import com.bugboard.api.models.User;

public interface Observer {
    void update(Issue issue, User user);
}
