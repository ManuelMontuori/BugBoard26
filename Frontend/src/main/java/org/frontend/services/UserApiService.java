package org.frontend.services;

import com.fasterxml.jackson.core.type.TypeReference;
import org.frontend.util.JsonUtil;
import org.frontend.models.User;

import java.net.http.HttpResponse;
import java.util.List;

public class UserApiService {

    private final ApiClient apiClient;

    public UserApiService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public List<User> findAll() throws Exception {
        var request = apiClient.request("/api/users")
                .GET()
                .build();

        HttpResponse<String> response = apiClient.client().send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode == 401) {
            throw new RuntimeException("401 Unauthorized: token mancante o non valido");
        }
        if (statusCode == 403) {
            throw new RuntimeException("403 Forbidden: token valido ma senza permessi");
        }
        if (statusCode == 404) {
            throw new RuntimeException("404 Not Found: endpoint /api/users non trovato");
        }
        if (statusCode < 200 || statusCode >= 300) {
            throw new RuntimeException("Errore HTTP " + statusCode + ": " + response.body());
        }

        return JsonUtil.mapper.readValue(response.body(), new TypeReference<>() {
        });
    }
}
