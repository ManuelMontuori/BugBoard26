package com.bugboard.api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.bugboard.api.dto.NotificationDTO;
import com.bugboard.api.mapper.NotificationMapper;
import com.bugboard.api.models.Issue;
import com.bugboard.api.models.Notification;
import com.bugboard.api.models.User;
import com.bugboard.api.repositories.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationWriteService service;

    // per catturare l'oggetto creato all'interno della classe da testare
    @Captor
    private ArgumentCaptor<Notification> notificationCaptor;

    @Test
    void testCreateNotificationValid() {

        Issue issue = new Issue();
        issue.setTitle("Errore login");
        issue.setDescription("L'utente non riesce ad accedere.");

        User user = new User();

        Notification savedNotification = new Notification();
        NotificationDTO expectedDto = new NotificationDTO(
                UUID.randomUUID().toString(), // uuid
                "Ti è stata assegnata la seguente Issue: Errore login\nDescrizione:L'utente non riesce ad accedere.", // message
                false, // read
                LocalDateTime.now() // createdAt
        );

        // Usiamo any() perché l'oggetto viene istanziato dentro il metodo
        when(notificationRepository.save(any(Notification.class)))
                .thenReturn(savedNotification);

        when(notificationMapper.mapToDTO(savedNotification))
                .thenReturn(expectedDto);

        NotificationDTO result = service.createNotification(issue, user);

        assertNotNull(result);
        assertEquals(expectedDto, result);

        verify(notificationRepository).save(notificationCaptor.capture());
        Notification capturedNotification = notificationCaptor.getValue();

        assertEquals(user, capturedNotification.getUser());
        assertFalse(capturedNotification.isRead());

        String expectedMessage = "Ti è stata assegnata la seguente Issue: Errore login\nDescrizione:L'utente non riesce ad accedere.";
        assertEquals(expectedMessage, capturedNotification.getMessage());

        verify(notificationMapper).mapToDTO(savedNotification);
    }

    @Test
    void testCreateNotificationWhenRepositoryThrowsException() {

        Issue issue = new Issue();
        issue.setTitle("Bug frontend");
        issue.setDescription("La dashboard non è responsive");
        User user = new User();

        when(notificationRepository.save(any(Notification.class)))
                .thenThrow(new RuntimeException("Errore di connessione al database"));

        assertThrows(RuntimeException.class, () -> service.createNotification(issue, user));

        verify(notificationMapper, never()).mapToDTO(any());
    }

}