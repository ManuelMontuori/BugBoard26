package com.bugboard.api.listeners;

import com.bugboard.api.events.IssueAssignedEvent;
import com.bugboard.api.services.NotificationService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

    private final NotificationService notificationService;

    public NotificationEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @EventListener
    public void onIssueAssigned(IssueAssignedEvent event) {
        notificationService.createNotification(event.getIssue(), event.getUser());
    }

}
