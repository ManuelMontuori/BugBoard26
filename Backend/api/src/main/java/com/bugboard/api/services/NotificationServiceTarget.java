package com.bugboard.api.services;

import java.util.Optional;
import java.util.UUID;

import com.bugboard.api.models.Notification;

public interface NotificationServiceTarget {

  public Notification save(Notification notification);

  public Optional<Notification> findByUuid(UUID uuid);
    

}
