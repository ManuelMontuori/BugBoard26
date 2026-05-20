package com.bugboard.api.observer;

import com.bugboard.api.models.Issue;
import com.bugboard.api.models.User;

public class NotificationObserver implements Observer {

//    private final NotificationService notificationService;
//    public NotificationObserver(NotificationService notificationService) {
//        this.notificationService = notificationService;
//    }

    @Override
    public void update(Issue issue, User user) {
        // usa metodo notificationService
    }
}
