package org.frontend.services;

import java.net.http.*;

public class IssueApiService {
        private final ApiClient apiClient;

        public IssueApiService(ApiClient apiClient) {

                this.apiClient = apiClient;

        }

        public String findAll() throws Exception {
                HttpRequest request = apiClient
                                .request("/api/issues")
                                .GET()
                                .build();

                HttpResponse<String> response = apiClient.client()
                                .send(
                                                request,
                                                HttpResponse.BodyHandlers.ofString());

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

                System.out.println("--- DEBUG RESPONSE SEARCH ---");
                System.out.println(response.body());
                System.out.println("-----------------------------");

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

                System.out.println("SIAMO NEL CREATE DEL api service");

                if (response.statusCode() >= 400) {
                        throw new RuntimeException(
                                        "Errore API: Staus " + response.statusCode() + " - " + response.body());
                }

                return response.body();
        }

}