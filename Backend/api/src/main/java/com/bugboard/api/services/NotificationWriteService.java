package com.bugboard.api.services;

import com.bugboard.api.dto.NotificationDTO;
import com.bugboard.api.mapper.NotificationMapper;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.Notification;
import com.bugboard.api.models.User;
import com.bugboard.api.repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.UUID;

@Service
public class NotificationWriteService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    public NotificationWriteService(NotificationRepository notificationRepository,
                                    NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    public NotificationDTO createNotification(Issue issue, User user) {

        Notification notification = new Notification();
        notification.setMessage("Ti è stata assegnata la seguente Issue: "
                + issue.getTitle() + "\nDescrizione:"
                + issue.getDescription());
        notification.setRead(false);
        notification.setUser(user);

        Notification savedNotification = notificationRepository.save(notification);

        return notificationMapper.mapToDTO(savedNotification);
    }

    public void readTrue(UUID uuid) {
        Notification notification = notificationRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceAccessException("Notification not found"));
        notification.setRead(true);

        notificationRepository.save(notification);

    }

    public void readFalse(UUID uuid) {
        Notification notification = notificationRepository.findByUuid(uuid)
        .orElseThrow(() -> new ResourceAccessException("Notification not found"));
        notification.setRead(false);
        notificationRepository.save(notification);
    }
}
