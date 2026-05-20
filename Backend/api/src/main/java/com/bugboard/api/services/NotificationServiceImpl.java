package com.bugboard.api.services;

import org.springframework.stereotype.Service;

import com.bugboard.api.mapper.NotificationMapper;

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

    

}
