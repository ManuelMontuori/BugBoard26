package org.frontend.services;

import java.net.http.*;

public class IssueApiService {
    private final ApiClient apiClient;

    public IssueApiService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public String findAll(String status, String priority, String type) throws Exception {
        StringBuilder urlBuilder = new StringBuilder("/api/issues?");

        if (status != null && !status.isBlank()) {
            urlBuilder.append("status=").append(java.net.URLEncoder.encode(status, java.nio.charset.StandardCharsets.UTF_8)).append("&");
        }
        if (priority != null && !priority.isBlank()) {
            urlBuilder.append("priority=").append(java.net.URLEncoder.encode(priority, java.nio.charset.StandardCharsets.UTF_8)).append("&");
        }
        if (type != null && !type.isBlank()) {
            urlBuilder.append("type=").append(java.net.URLEncoder.encode(type, java.nio.charset.StandardCharsets.UTF_8)).append("&");
        }

        // Rimuove l'ultimo carattere '&' o '?' se non ci sono filtri
        String url = urlBuilder.toString();
        if (url.endsWith("&") || url.endsWith("?")) {
            url = url.substring(0, url.length() - 1);
        }

        HttpRequest request = apiClient
                .request(url)
                .GET()
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException("Errore API findAll: Status " + response.statusCode() + " - " + response.body());
        }

        return response.body();
    }

    public String findAssignedToMe(String userUuid) throws Exception {

        HttpRequest request = apiClient
                .request("/api/issues/assigned?assignedTo=" + userUuid)
                .GET()
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

        return response.body();
    }

    public String searchIssue(String keyword) throws Exception {

        String encodedKeyword = java.net.URLEncoder.encode(keyword, java.nio.charset.StandardCharsets.UTF_8);

        HttpRequest request = apiClient
                .request("/api/issues/search?keyword=" + encodedKeyword)
                .GET()
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException(
                    "Errore API Cerca: Status " + response.statusCode() + " - " + response.body());
        }

        return response.body();
    }

    public String create(String jsonPayload) throws Exception {
        HttpRequest request = apiClient
                .request("/api/issues")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = apiClient.client()
                .send(
                        request,
                        HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 400) {
            throw new RuntimeException(
                    "Errore API: Staus " + response.statusCode() + " - " + response.body());
        }

        return response.body();
    }

}