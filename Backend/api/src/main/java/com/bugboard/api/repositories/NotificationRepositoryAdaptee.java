package com.bugboard.api.repositories;

import com.bugboard.api.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepositoryAdaptee extends JpaRepository<Notification, Long> {

    


    
}
