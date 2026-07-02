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

    private static final String CLIENT_ID =
            "2m5mjtr6jk7ht41lv9e57596t2";

    private static final String REDIRECT_URI =
            "http://localhost:9090/callback";

    // pkce
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

        try {
            if (code == null || code.isBlank()) {
                throw new IllegalArgumentException("Missing authorization code");
            }
            if (codeVerifier == null || codeVerifier.isBlank()) {
                throw new IllegalStateException("Missing PKCE code_verifier. Devi chiamare buildHostedUiLoginUrl() prima del login.");
            }

            HttpClient client = HttpClient.newHttpClient();

            String body =
                    "grant_type=authorization_code"
                            + "&client_id=" + url(CLIENT_ID)
                            + "&code=" + url(code)
                            + "&redirect_uri=" + url(REDIRECT_URI)
                            + "&code_verifier=" + url(codeVerifier);

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(DOMAIN + "/oauth2/token"))
                            .header("Content-Type", "application/x-www-form-urlencoded")
                            .POST(HttpRequest.BodyPublishers.ofString(body))
                            .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                JsonNode node = JsonUtil.mapper.readTree(response.body());

                String access    = node.path("access_token").asText("");
                String refresh   = node.path("refresh_token").asText("");
                String id        = node.path("id_token").asText("");
                int    expiresIn = node.path("expires_in").asInt(3600);

                if (!access.isBlank()) {
                    AuthSession.getInstance().setTokens(access, refresh, id, expiresIn);

                    // Notifica la UI JavaFX
                    Platform.runLater(() -> LoginEvent.fire());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        session.clear();
    }
    public static String buildHostedUiLogoutUrl() {

        return DOMAIN + "/logout"
                + "?client_id=" + url(CLIENT_ID)
                + "&logout_uri=" + url(REDIRECT_URI);
    }

    private static String url(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}