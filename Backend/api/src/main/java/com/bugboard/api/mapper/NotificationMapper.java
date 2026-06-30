package com.bugboard.api.mapper;

import com.bugboard.api.dto.NotificationDTO;
import com.bugboard.api.models.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public NotificationDTO mapToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getUuid().toString(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );
    }
}
