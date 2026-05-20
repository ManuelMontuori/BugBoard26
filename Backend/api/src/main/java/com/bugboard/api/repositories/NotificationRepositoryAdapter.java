package com.bugboard.api.repositories;


import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.bugboard.api.models.Notification;
import com.bugboard.api.services.NotificationServiceTarget;

@Component
public class NotificationRepositoryAdapter implements NotificationServiceTarget {
    private final NotificationRepositoryAdaptee notificationRepositoryAdaptee;

    public NotificationRepositoryAdapter(NotificationRepositoryAdaptee notificationRepositoryAdaptee) {
        this.notificationRepositoryAdaptee = notificationRepositoryAdaptee;
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepositoryAdaptee.save(notification);
    }

    @Override
    public Optional<Notification> findByUuid(UUID uuid) {
        return notificationRepositoryAdaptee.findByUuid(uuid);
    }



}
