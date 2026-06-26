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

    public String findByWorkload() throws Exception {
        HttpRequest request = apiClient
                .request("/api/users/workload")
                .GET()
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public void assignIssue(String issueUuid, String userUuid) throws Exception {
        HttpRequest request = apiClient
                .request("/api/issues/assigned?issueUuid=" + issueUuid + "&userUuid=" + userUuid)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("--- DEBUG ASSIGN STATUS ---");
        System.out.println(response.statusCode());
        System.out.println("---------------------------");

        if (response.statusCode() >= 400)
            throw new RuntimeException("Errore API: " + response.statusCode() + " - " + response.body());
    }

    public String getMonthlyReport(int year, int month) throws Exception {
        HttpRequest request = apiClient
                .request("/api/users/report?year=" + year + "&month=" + month)
                .GET()
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public void enable(String uuid) throws Exception {
        HttpRequest request = apiClient
                .request("/api/users/enable/" + uuid) // Assicurati che il prefisso rispetti il tuo backend (es. /api/users o solo /api)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(request, HttpResponse.BodyHandlers.ofString());

        // 🚨 LOG DI DEBUG IMPERATIVO
        System.out.println("--- DEBUG PATCH ENABLE ---");
        System.out.println("Status: " + response.statusCode());
        System.out.println("--------------------------");

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Errore API Abilita: Status " + response.statusCode() + " - " + response.body());
        }
    }

    public void disable(String uuid) throws Exception {
        HttpRequest request = apiClient
                .request("/api/users/disable/" + uuid)
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(request, HttpResponse.BodyHandlers.ofString());

        // 🚨 LOG DI DEBUG IMPERATIVO
        System.out.println("--- DEBUG PATCH DISABLE ---");
        System.out.println("Status: " + response.statusCode());
        System.out.println("---------------------------");

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Errore API Disabilita: Status " + response.statusCode() + " - " + response.body());
        }
    }
}