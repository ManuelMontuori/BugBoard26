package com.bugboard.api.listeners;

import com.bugboard.api.dto.NotificationDTO;
import com.bugboard.api.events.IssueAssignedEvent;
import com.bugboard.api.services.NotificationService;
import com.bugboard.api.services.NotificationStreamService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class NotificationEventListener {

    private final NotificationService notificationService;
    private final NotificationStreamService  notificationStreamService;

    public NotificationEventListener(NotificationService notificationService,
                                     NotificationStreamService notificationStreamService) {
        this.notificationService = notificationService;
        this.notificationStreamService = notificationStreamService;
    }

    @EventListener
    public void onIssueAssigned(IssueAssignedEvent event) {
        NotificationDTO dto=  notificationService.createNotification(event.getIssue(), event.getUser());
        // invia sulla rete in tempo reale con sse
        UUID userUuid = event.getUser().getUuid();
        notificationStreamService.sendRealTimeNotification(userUuid, dto);
    }

}
