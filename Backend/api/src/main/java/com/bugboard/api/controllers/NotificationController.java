package com.bugboard.api.controllers;

import java.util.List;
import java.util.UUID;

import com.bugboard.api.dto.NotificationDTO;
import com.bugboard.api.services.NotificationStreamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.bugboard.api.services.NotificationService;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationStreamService notificationStreamService;


    public NotificationController(NotificationService notificationService,
                                  NotificationStreamService notificationStreamService) {
        this.notificationService = notificationService;
        this.notificationStreamService = notificationStreamService;
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

    @GetMapping(value= "/stream/{uuid}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamNotifications(@PathVariable UUID uuid) {
        return notificationStreamService.createStream(uuid); // Chiama il servizio di stream
    }

}
