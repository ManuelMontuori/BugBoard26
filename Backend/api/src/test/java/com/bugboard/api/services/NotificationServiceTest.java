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

// Sostituisci con il nome del tuo service
@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private NotificationMapper notificationMapper;

    @InjectMocks
    private NotificationWriteService service;


    // Definiamo il "catturatore" per oggetti di tipo Notification
    @Captor
    private ArgumentCaptor<Notification> notificationCaptor;

    @Test
    void testCreateNotificationSuccess() {
        // 1. Arrange: Prepariamo i dati in ingresso
        Issue issue = new Issue();
        issue.setTitle("Errore login");
        issue.setDescription("L'utente non riesce ad accedere.");

        User user = new User();
        // user.setId(UUID.randomUUID()); // Opzionale, se serve al tuo User

        // Prepariamo le risposte fittizie per repository e mapper
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

        // 2. Act: Eseguiamo il metodo
        NotificationDTO result = service.createNotification(issue, user);

        // 3. Assert: Controlliamo che il risultato finale sia quello mappato
        assertNotNull(result);
        assertEquals(expectedDto, result);

        // 4. VERIFICA INTERNA (La parte più importante)
        // Catturiamo la notifica nel momento esatto in cui viene salvata
        verify(notificationRepository).save(notificationCaptor.capture());
        Notification capturedNotification = notificationCaptor.getValue();

        // Ora possiamo testare che i campi siano stati riempiti correttamente!
        assertEquals(user, capturedNotification.getUser());
        assertFalse(capturedNotification.isRead());

        String expectedMessage = "Ti è stata assegnata la seguente Issue: Errore login\nDescrizione:L'utente non riesce ad accedere.";
        assertEquals(expectedMessage, capturedNotification.getMessage());

        // Verifichiamo che il mapper sia stato chiamato con l'oggetto salvato dal DB
        verify(notificationMapper).mapToDTO(savedNotification);
    }

    @Test
    void testCreateNotificationWhenRepositoryThrowsException() {
        // 1. Arrange
        Issue issue = new Issue();
        issue.setTitle("Bug frontend");
        issue.setDescription("La dashboard non è responsive");
        User user = new User();

        // Istruiamo il finto repository a simulare un crash (es. connessione persa)
        when(notificationRepository.save(any(Notification.class)))
                .thenThrow(new RuntimeException("Errore di connessione al database"));

        // 2 & 3. Act & Assert
        // Verifichiamo che l'eccezione del database venga propagata a chi ha chiamato il metodo
        assertThrows(RuntimeException.class, () -> {
            service.createNotification(issue, user);
        });

        // 4. Verify
        // Ci assicuriamo che il mapper non venga MAI chiamato se il salvataggio è fallito
        verify(notificationMapper, never()).mapToDTO(any());
    }

}