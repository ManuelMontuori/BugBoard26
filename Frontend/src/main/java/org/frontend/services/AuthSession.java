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

    // --- Dati estratti dal JWT (Memorizzati per efficienza) ---
    private String email = "";
    private String customUuid = "";
    private String role = "";


    private StringProperty username = new SimpleStringProperty();

    // --- properties osservabili per i binding JavaFX ---
    private final BooleanProperty authenticated = new SimpleBooleanProperty(false);
    private final StringProperty  displayName   = new SimpleStringProperty("");
    private final StringProperty  displayRole = new SimpleStringProperty("");

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

        // Estraiamo i dati dall'ID Token una volta sola qui
        estraiDatiDaIdToken(id);

        this.authenticated.set(true);
    }

    // --- Metodo privato di parsing (eseguito UNA SOLA VOLTA al login) ---
    private void estraiDatiDaIdToken(String idTokenStr) {
        if (idTokenStr == null || idTokenStr.isBlank()) {
            this.email = "";
            this.customUuid = "";
            return;
        }
        try {
            String[] parts   = idTokenStr.split("\\.");
            if (parts.length < 2) return;

            String   payload = parts[1];
            int pad = payload.length() % 4;
            if (pad != 0) payload += "=".repeat(4 - pad);

            byte[] decoded = java.util.Base64.getUrlDecoder().decode(payload);
            ObjectMapper mapper = new ObjectMapper();
            JsonNode node   = mapper.readTree(decoded);

            // Aggiungi questo nel tuo blocco try dopo "JsonNode node = mapper.readTree(decoded);"
            System.out.println("Payload JWT completo: " + node.toString());
            // 1. Estrai Email
            this.email = node.path("email").asText("");

            // 2. Estrai il custom::uuid da Cognito
            this.customUuid = node.path("custom::uuid").asText("");

            this.role = node.path("custom::role").asText("");
            System.out.println("uuid:" + customUuid);
            System.out.println("email:" + email);
            System.out.println("ruolo:" + role);

            // 3. Opzionale: Imposta in automatico il displayName usando l'email o un altro campo (es. "name")
            if (node.has("name")) {
                this.displayName.set(node.path("name").asText(""));
            } else {
                this.displayName.set(this.email);
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.email = "";
            this.customUuid = "";
        }
    }

    // --- getters token ---
    public String getAccessToken()  { return accessToken;  }
    public String getRefreshToken() { return refreshToken; }
    public String getIdToken()      { return idToken;      }

    // --- getters dati utente (Ottenimento istantaneo senza parsing ripetuto) ---
    public String getEmail()        { return email;        }
    public String getCustomUuid()   { return customUuid;   }

    // --- stato sessione ---
    public boolean isAuthenticated() {
        return accessToken != null
                && !accessToken.isBlank()
                && Instant.now().getEpochSecond() < accessTokenExpiresAt;
    }

    public boolean canRefresh() {
        return refreshToken != null && !refreshToken.isBlank();
    }

    // --- properties JavaFX per i binding ---
    public BooleanProperty authenticatedProperty() { return authenticated; }
    public StringProperty  displayNameProperty()   { return displayName;   }
    public StringProperty  displayRoleProperty() { return displayRole; }

    // --- logout locale ---
    public void clear() {
        accessToken = refreshToken = idToken = null;
        accessTokenExpiresAt = 0;
        email = "";
        customUuid = "";
        authenticated.set(false);
        displayName.set("");
    }
}