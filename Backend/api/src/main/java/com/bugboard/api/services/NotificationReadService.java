package com.bugboard.api.services;

import com.bugboard.api.auth.AuthService;
import com.bugboard.api.dto.NotificationDTO;
import com.bugboard.api.mapper.NotificationMapper;
import com.bugboard.api.models.User;
import com.bugboard.api.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationReadService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final AuthService authService;

    public NotificationReadService(NotificationRepository notificationRepository,
                                   NotificationMapper notificationMapper,
                                   AuthService authService) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.authService = authService;
    }

    public List<NotificationDTO> myNotifications() {
        User user = authService.getCurrentUser().orElseThrow(() -> new RuntimeException("User not found"));
        return notificationRepository.findByUserId(user.getId()).stream()
                .map(notificationMapper::mapToDTO)
                .toList();
    }
}
