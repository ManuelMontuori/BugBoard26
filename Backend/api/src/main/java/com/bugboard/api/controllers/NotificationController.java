package com.bugboard.api.controllers;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bugboard.api.services.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;



    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    
    @PatchMapping("/read/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void readTrue(@RequestParam UUID uuid) {
        notificationService.readTrue(uuid);
    }

}
