package org.frontend.services;

import java.net.http.*;

public class IssueApiService {
    private final ApiClient apiClient;

    public IssueApiService(ApiClient apiClient){

        this.apiClient = apiClient;

    }

    public String findAll() throws Exception {
        HttpRequest request =
                apiClient
                        .request("/api/issues")
                        .GET()
                        .build();

        HttpResponse<String> response =
                apiClient.client()
                        .send(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        );

        return response.body();

    }

    public String findAssignedToMe(String userUuid) throws Exception {
        // Adatta l'URL alla struttura esatta del tuo backend (es. /api/issues/assigned o con query param)
        HttpRequest request =
                apiClient
                        .request("/api/issues/assigned?assignedTo=" + userUuid)
                        .GET()
                        .build();

        HttpResponse<String> response =
                apiClient.client()
                        .send(
                                request,
                                HttpResponse.BodyHandlers.ofString()
                        );

        return response.body();
    }

}