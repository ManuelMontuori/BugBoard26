package com.bugboard.api.services;

import com.bugboard.api.dto.NotificationDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationStreamService {

    // La mappa è una normale proprietà di un Singleton Spring
    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    
    public SseEmitter createStream(UUID userUuid) {
        SseEmitter emitter = new SseEmitter(0L); 

        this.emitters.put(userUuid, emitter);

        emitter.onCompletion(() -> this.emitters.remove(userUuid));
        emitter.onTimeout(() -> this.emitters.remove(userUuid));
        emitter.onError(e -> this.emitters.remove(userUuid));

        return emitter;
    }

    
    public void sendRealTimeNotification(UUID userUuid, NotificationDTO dto) {
        SseEmitter emitter = emitters.get(userUuid);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("message")
                        .data(dto, MediaType.APPLICATION_JSON));
            } catch (Exception e) {
                emitters.remove(userUuid);
            }
        }
    }
}