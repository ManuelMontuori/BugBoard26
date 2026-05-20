package com.bugboard.api.services;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.bugboard.api.mapper.NotificationMapper;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.Notification;
import com.bugboard.api.models.User;

import jakarta.transaction.Transactional;


@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {
    private final NotificationServiceTarget notificationServiceTarget;
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationServiceTarget notificationServiceTarget, NotificationMapper notificationMapper) {
        this.notificationServiceTarget = notificationServiceTarget;
        this.notificationMapper = notificationMapper;
    }

    @Override
    public void createNotification(Issue issue, User user) {

        Notification notification = new Notification();
        notification.setMessage("Ti è stata assegnata la seguente Issue: " + issue.getTitle() + "\nDescrizione:"
                + issue.getDescription());
        notification.setRead(false);
        notification.setUser(user);
       
        notificationServiceTarget.save(notification);

    }

    @Override
    public void readTrue(UUID uuid) {
        Notification notification = notificationServiceTarget.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);

        notificationServiceTarget.save(notification);
        
        
        
    }


    

}
