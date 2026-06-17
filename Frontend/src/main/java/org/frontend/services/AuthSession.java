package org.frontend.services;

import java.time.Instant;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class AuthSession {

    private static final AuthSession INSTANCE = new AuthSession();

    // --- token grezzi ---
    private String accessToken;
    private String refreshToken;
    private String idToken;
    private long   accessTokenExpiresAt; // epoch-seconds

    // --- properties osservabili per i binding JavaFX ---
    private final BooleanProperty authenticated = new SimpleBooleanProperty(false);
    private final StringProperty  displayName   = new SimpleStringProperty("");

    private AuthSession() {}

    public static AuthSession getInstance() {
        return INSTANCE;
    }

    // --- setter principale (chiamato da CognitoAuthService) ---

    public void setTokens(String access, String refresh, String id, int expiresIn) {
        this.accessToken          = access;
        this.refreshToken         = refresh;
        this.idToken              = id;
        this.accessTokenExpiresAt = Instant.now().getEpochSecond() + expiresIn - 60;

        authenticated.set(true);
        // displayName lo imposti tu quando decodifichi l'id_token
    }

    // --- getters token ---

    public String getAccessToken()  { return accessToken;  }
    public String getRefreshToken() { return refreshToken; }
    public String getIdToken()      { return idToken;      }

    // --- stato sessione ---

    /** true se il token è presente e non ancora scaduto */
    public boolean isAuthenticated() {
        return accessToken != null
                && !accessToken.isBlank()
                && Instant.now().getEpochSecond() < accessTokenExpiresAt;
    }

    /** true se possiamo tentare un refresh senza rifare il login */
    public boolean canRefresh() {
        return refreshToken != null && !refreshToken.isBlank();
    }

    // --- properties JavaFX per i binding ---

    public BooleanProperty authenticatedProperty() { return authenticated; }
    public StringProperty  displayNameProperty()   { return displayName;   }

    // --- logout locale ---

    public void clear() {
        accessToken = refreshToken = idToken = null;
        accessTokenExpiresAt = 0;
        authenticated.set(false);
        displayName.set("");
    }

    public String getEmail() {
        if (idToken == null || idToken.isBlank()) return "";
        try {
            String[] parts   = idToken.split("\\.");
            String   payload = parts[1];
            int pad = payload.length() % 4;
            if (pad != 0) payload += "=".repeat(4 - pad);

            byte[] decoded = java.util.Base64.getUrlDecoder().decode(payload);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node   = mapper.readTree(decoded);
            return node.path("email").asText("");

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}