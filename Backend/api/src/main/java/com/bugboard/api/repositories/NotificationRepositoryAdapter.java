package com.bugboard.api.repositories;

import org.springframework.stereotype.Component;

import com.bugboard.api.services.NotificationServiceTarget;

@Component
public class NotificationRepositoryAdapter implements NotificationServiceTarget {
    private final NotificationRepositoryAdaptee notificationRepositoryAdaptee;

    public NotificationRepositoryAdapter(NotificationRepositoryAdaptee notificationRepositoryAdaptee) {
        this.notificationRepositoryAdaptee = notificationRepositoryAdaptee;
    }

    



}
