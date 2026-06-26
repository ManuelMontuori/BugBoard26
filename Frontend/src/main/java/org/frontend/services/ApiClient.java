package org.frontend.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;

public class ApiClient {

    private final HttpClient httpClient;
    private final String baseUrl;

    public ApiClient(String baseUrl) {
        this.baseUrl = baseUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public HttpClient client() {
        return httpClient;
    }

    // Metodo esistente — invariato, usato da tutte le chiamate normali
    public HttpRequest.Builder request(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                .timeout(Duration.ofSeconds(30))
                .header("Accept", "application/json");

        String token = AuthSession.getInstance().getIdToken();
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        return builder;
    }

    // Metodo NUOVO — solo per SSE, nessun timeout di request
    public HttpRequest.Builder requestStream(String path) {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + path))
                // NO .timeout() — la connessione SSE deve restare aperta
                .header("Accept", "text/event-stream");

        String token = AuthSession.getInstance().getIdToken();
        if (token != null && !token.isBlank()) {
            builder.header("Authorization", "Bearer " + token);
        }

        return builder;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}