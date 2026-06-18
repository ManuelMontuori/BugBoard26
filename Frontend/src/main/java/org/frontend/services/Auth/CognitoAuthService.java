package org.frontend.services.Auth;

import com.fasterxml.jackson.databind.JsonNode;
import javafx.application.Platform;
import org.frontend.util.JsonUtil;
import org.frontend.services.AuthSession;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class CognitoAuthService {

    private static final String DOMAIN =
            "https://eu-south-1aa4g67ut3.auth.eu-south-1.amazoncognito.com";

    // IMPORTANT: questo deve essere lo stesso del link Hosted UI che apri
    private static final String CLIENT_ID =
            "2m5mjtr6jk7ht41lv9e57596t2";

    private static final String REDIRECT_URI =
            "http://localhost:9090/callback";

    // PKCE: lo generi prima del login e lo riusi nel token exchange
    private static String codeVerifier;

    public static String buildHostedUiLoginUrl() {
        codeVerifier = PkceUtil.generateCodeVerifier();
        String codeChallenge = PkceUtil.codeChallengeS256(codeVerifier);

        String scope = "email openid profile";

        return DOMAIN + "/oauth2/authorize"
                + "?response_type=code"
                + "&client_id=" + url(CLIENT_ID)
                + "&redirect_uri=" + url(REDIRECT_URI)
                + "&scope=" + url(scope)
                + "&code_challenge=" + url(codeChallenge)
                + "&code_challenge_method=S256";
    }

    public static void exchangeCode(String code) {
        System.out.println(">>> exchangeCode START");

        try {
            if (code == null || code.isBlank()) {
                throw new IllegalArgumentException("Missing authorization code");
            }
            if (codeVerifier == null || codeVerifier.isBlank()) {
                throw new IllegalStateException("Missing PKCE code_verifier. Devi chiamare buildHostedUiLoginUrl() prima del login.");
            }

            System.out.println("CODE = " + code);

            HttpClient client = HttpClient.newHttpClient();

            String body =
                    "grant_type=authorization_code"
                            + "&client_id=" + url(CLIENT_ID)
                            + "&code=" + url(code)
                            + "&redirect_uri=" + url(REDIRECT_URI)
                            + "&code_verifier=" + url(codeVerifier);

            System.out.println("BODY = " + body);

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(DOMAIN + "/oauth2/token"))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .POST(HttpRequest.BodyPublishers.ofString(body))
                            .build();

            System.out.println("REQUEST SENT");

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("STATUS = " + response.statusCode());
            System.out.println("BODY = " + response.body());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonNode node = JsonUtil.mapper.readTree(response.body());

                String access    = node.path("access_token").asText("");
                String refresh   = node.path("refresh_token").asText("");
                String id        = node.path("id_token").asText("");
                int    expiresIn = node.path("expires_in").asInt(3600);

                if (!access.isBlank()) {
                    AuthSession.getInstance().setTokens(access, refresh, id, expiresIn);
                    System.out.println("Tokens salvati in sessione.");

                    // Notifica la UI JavaFX (che gira sull'FX thread)
                    Platform.runLater(() -> LoginEvent.fire());
                }
            }

        } catch (Exception e) {
            System.out.println(">>> ERROR OCCURRED");
            e.printStackTrace();
        }

        System.out.println(">>> exchangeCode END");
    }

    public static boolean refreshAccessToken() {
        AuthSession session = AuthSession.getInstance();
        if (!session.canRefresh()) return false;

        try {
            HttpClient client = HttpClient.newHttpClient();

            String body =
                    "grant_type=refresh_token"
                            + "&client_id=" + url(CLIENT_ID)
                            + "&refresh_token=" + url(session.getRefreshToken());

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(DOMAIN + "/oauth2/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonNode node = JsonUtil.mapper.readTree(response.body());
                String access    = node.path("access_token").asText("");
                String id        = node.path("id_token").asText("");
                // Cognito NON restituisce un nuovo refresh token qui
                int    expiresIn = node.path("expires_in").asInt(3600);

                if (!access.isBlank()) {
                    // refresh token rimane invariato
                    session.setTokens(access, session.getRefreshToken(), id, expiresIn);
                    System.out.println("Access token rinnovato.");
                    return true;
                }
            }

            System.err.println("Refresh fallito: " + response.statusCode() + " " + response.body());
            return false;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Revoca il refresh token su Cognito e pulisce la sessione locale.
     * Dopo questa chiamata l'utente deve rifare il login.
     */
    public static void logout() {
        AuthSession session = AuthSession.getInstance();
        String token = session.getRefreshToken();

        if (token != null && !token.isBlank()) {
            try {
                HttpClient client = HttpClient.newHttpClient();

                String body =
                        "token=" + url(token)
                                + "&client_id=" + url(CLIENT_ID);

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(DOMAIN + "/oauth2/revoke"))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(HttpRequest.BodyPublishers.ofString(body))
                        .build();

                HttpResponse<String> response =
                        client.send(request, HttpResponse.BodyHandlers.ofString());

                System.out.println("Revoke status: " + response.statusCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        session.clear();
    }

    private static String url(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}