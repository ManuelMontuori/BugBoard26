package com.bugboard.api.repositories;

import com.bugboard.api.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepositoryAdaptee extends JpaRepository<Notification, Long> {
    
}
