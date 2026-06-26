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
                .requestStream("/api/notification/stream/" + userUuid) // <-- requestStream, non request
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

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

    public String findMyNotifications() throws Exception {
        HttpRequest request = apiClient
                .request("/api/notification/my")
                .GET()
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Errore API notifiche: " + response.statusCode());
        }
        return response.body();
    }

    public void setRead(String uuid, boolean check) throws Exception {
        HttpRequest request = apiClient
                .request("/api/notification/read?uuid=" + uuid + "&check=" + check)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Errore PATCH notifica: " + response.statusCode());
        }
    }


}