package com.bugboard.api.services;

import java.util.List;
import java.util.UUID;
import com.bugboard.api.dto.NotificationDTO;
import org.springframework.stereotype.Service;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.User;
import jakarta.transaction.Transactional;


@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationReadService notificationReadService;
    private final NotificationWriteService notificationWriteService;
    public NotificationServiceImpl(NotificationReadService notificationReadService,
                                   NotificationWriteService notificationWriteService) {
        this.notificationReadService = notificationReadService;
        this.notificationWriteService = notificationWriteService;
    }

    @Override
    public void createNotification(Issue issue, User user) {
        notificationWriteService.createNotification(issue, user);
    }

    @Override
    public void readTrue(UUID uuid) {
        notificationWriteService.readTrue(uuid);

    }

    @Override
    public void readFalse(UUID uuid) {
        notificationWriteService.readFalse(uuid);
    }

    @Override
    public List<NotificationDTO> myNotifications() {
        return notificationReadService.myNotifications();
    }


}
