package com.bugboard.api.services;

import com.bugboard.api.models.Issue;
import com.bugboard.api.models.Notification;
import com.bugboard.api.models.User;
import com.bugboard.api.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationWriteService {
    private final NotificationRepository notificationRepository;
    public NotificationWriteService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(Issue issue, User user) {

        Notification notification = new Notification();
        notification.setMessage("Ti è stata assegnata la seguente Issue: "
                + issue.getTitle() + "\nDescrizione:"
                + issue.getDescription());
        notification.setRead(false);
        notification.setUser(user);

        notificationRepository.save(notification);

    }

    public void readTrue(UUID uuid) {
        Notification notification = notificationRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);

        notificationRepository.save(notification);

    }

    public void readFalse(UUID uuid) {
        Notification notification = notificationRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(false);
        notificationRepository.save(notification);
    }
}
