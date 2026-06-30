package com.bugboard.api.repositories;

import com.bugboard.api.models.Notification;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;



public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByUuid(UUID uuid);
    List<Notification> findByUserId(Long userId);
}

