package com.bugboard.api.services;

import java.util.List;
import java.util.UUID;

import com.bugboard.api.auth.AuthService;
import com.bugboard.api.dto.NotificationDTO;
import com.bugboard.api.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import com.bugboard.api.mapper.NotificationMapper;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.Notification;
import com.bugboard.api.models.User;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final AuthService authService;

    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   NotificationMapper notificationMapper,
                                   AuthService authService) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.authService = authService;
    }

    @Override
    public void createNotification(Issue issue, User user) {

        Notification notification = new Notification();
        notification.setMessage("Ti è stata assegnata la seguente Issue: " + issue.getTitle() + "\nDescrizione:"
                + issue.getDescription());
        notification.setRead(false);
        notification.setUser(user);

        notificationRepository.save(notification);

    }

    @Override
    public void readTrue(UUID uuid) {
        Notification notification = notificationRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);

        notificationRepository.save(notification);

    }

    @Override
    public void readFalse(UUID uuid) {
        Notification notification = notificationRepository.findByUuid(uuid).orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(false);
        notificationRepository.save(notification);
    }

    @Override
    public List<NotificationDTO> myNotifications() {
        User user = authService.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUserId(user.getId()).stream()
                .map(notificationMapper::mapToDTO)
                .toList();
    }


}
