package org.frontend.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.frontend.models.Notification;

import java.net.http.HttpResponse;
import java.util.stream.Stream;

public class NotificationService {

    private final NotificationApiService api;
    private final ObjectMapper objectMapper;
    private final ObservableList<Notification> notifications = FXCollections.observableArrayList();

    public NotificationService(NotificationApiService api, ObjectMapper objectMapper) {
        this.api = api;
        this.objectMapper = objectMapper;
    }

    public ObservableList<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Avvia il thread demone in ascolto continuo delle notifiche push
     */
    public void startNotificationListener(String userUuid, String token) {
        Thread sseThread = new Thread(() -> {
            try {
                System.out.println("NotificationService: Tentativo connessione SSE...");

                // Chiamiamo l'api service passando token e UUID
                HttpResponse<Stream<String>> response = api.streamNotifications(userUuid, token);

                if (response.statusCode() == 200) {
                    System.out.println("NotificationService: Flusso SSE connesso!");

                    try (Stream<String> lines = response.body()) {
                        lines.forEach(line -> {
                            if (line.startsWith("data:")) {
                                String jsonPayload = line.substring(5).trim();
                                System.out.println("ARRIVATA NUOVA NOTIFICA NEL SERVICE");
                                processNotificationJson(jsonPayload);
                            }
                        });
                    }
                } else {
                    System.err.println("NotificationService: Connessione rifiutata. Status: " + response.statusCode());
                }

            } catch (Exception e) {
                System.err.println("NotificationService: Connessione caduta. Riconnessione tra 5s...");
                e.printStackTrace();

                // Tentativo di riconnessione automatica
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}
                startNotificationListener(userUuid, token);
            }
        });

        sseThread.setDaemon(true); // Fondamentale: si spegne alla chiusura dell'app
        sseThread.start();
    }

    private void processNotificationJson(String json) {
        try {
            Notification notification = objectMapper.readValue(json, Notification.class);

            // Portiamo la modifica sul thread grafico di JavaFX
            Platform.runLater(() -> {
                notifications.add(0, notification); // Inserisce in testa alla lista
            });
        } catch (Exception e) {
            System.err.println("NotificationService: Errore parsing notifica JSON: " + e.getMessage());
        }
    }
}