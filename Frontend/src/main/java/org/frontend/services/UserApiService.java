package org.frontend.services;

import java.net.http.*;

public class UserApiService {

    private final ApiClient apiClient;

    public UserApiService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public String findAll() throws Exception {
        HttpRequest request = apiClient
                .request("/api/users")
                .GET()
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public String create(String jsonPayload) throws Exception {
        HttpRequest request = apiClient
                .request("/api/users")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("--- DEBUG USER API CREATE STATUS ---");
        System.out.println(response.statusCode());
        System.out.println("------------------------------------");

        if (response.statusCode() >= 400) {
            throw new RuntimeException(
                    "Errore API: Status " + response.statusCode() + " - " + response.body()
            );
        }

        return response.body();
    }
}