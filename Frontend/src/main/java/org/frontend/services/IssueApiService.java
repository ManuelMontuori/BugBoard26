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

}