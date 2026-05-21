package com.bugboard.api.repositories;

import com.bugboard.api.dto.NotificationDTO;
import com.bugboard.api.models.Notification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepositoryAdaptee extends JpaRepository<Notification, Long> {

    public Optional<Notification> findByUuid(UUID uuid);
    public List<Notification> findByUserId(Long userId);
    
}

