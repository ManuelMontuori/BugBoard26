package com.bugboard.api.controllers;

import java.util.List;
import java.util.UUID;

import com.bugboard.api.dto.NotificationDTO;
import com.bugboard.api.models.Notification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.bugboard.api.services.NotificationService;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;



    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    
    @PatchMapping("/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void readTrue(@RequestParam UUID uuid, @RequestParam boolean check) {
        if(check)
            notificationService.readTrue(uuid);
        else
            notificationService.readFalse(uuid);
    }

    @GetMapping("/my")
    public List<NotificationDTO> myNotifications() {
        return notificationService.myNotifications();
    }

}
