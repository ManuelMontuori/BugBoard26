package org.frontend.services;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Stream;

public class NotificationApiService {

    private final ApiClient apiClient; // Presumo tu abbia una classe base ApiClient per configurare l'host

    public NotificationApiService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Apre la connessione persistente SSE verso il server inviando il token di autenticazione.
     */
    public HttpResponse<Stream<String>> streamNotifications(String userUuid, String token) throws Exception {
        HttpRequest request = apiClient
                .request("/api/notification/stream/" + userUuid) // Adatta il path al tuo backend
                .header("Accept", "text/event-stream")
                .header("Authorization", "Bearer " + token) // Il solito Token JWT
                .GET()
                .build();

        // Mandiamo la richiesta aspettandoci un flusso continuo di righe (lines)
        return apiClient.client().send(request, HttpResponse.BodyHandlers.ofLines());
    }

    /**
     * Esempio di chiamata PATCH standard per segnare la notifica come letta
     */
    public void markAsRead(String notificationId, String token) throws Exception {
        HttpRequest request = apiClient
                .request("/api/notification/" + notificationId + "/read")
                .header("Authorization", "Bearer " + token)
                .timeout(java.time.Duration.ofDays(1))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Errore API notifica letta: " + response.statusCode());
        }
    }
}