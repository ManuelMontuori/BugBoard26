package com.bugboard.api.services;

import java.util.List;
import java.util.UUID;

import com.bugboard.api.dto.NotificationDTO;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.User;


public interface NotificationService {


    public NotificationDTO createNotification (Issue issue, User user);

    public void readTrue(UUID uuid);

    public void readFalse(UUID uuid);
    
    public List<NotificationDTO> myNotifications();
    

}
