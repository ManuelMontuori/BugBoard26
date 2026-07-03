package org.frontend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public final class AuthSession {
    private static final AuthSession INSTANCE = new AuthSession();

    private NotificationService notificationService;

    private String refreshToken;
    private String idToken;

    private String email = "";
    private String customUuid = "";
    private String role = "";

    private final BooleanProperty authenticated = new SimpleBooleanProperty(false);
    private final StringProperty  displayName   = new SimpleStringProperty("");
    private final StringProperty  displayRole = new SimpleStringProperty("");

    private AuthSession() {}

    public static AuthSession getInstance() {
        return INSTANCE;
    }

    public void setTokens(String access, String refresh, String id, int expiresIn) {
        this.refreshToken         = refresh;
        this.idToken              = id;

        estraiDatiDaIdToken(id);

        inizializzaFlussoNotifiche();

        this.authenticated.set(true);
    }

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

            this.email = node.path("email").asText("");

            this.customUuid = node.path("custom:uuid").asText("");

            this.role = node.path("custom:role").asText("");

            this.displayRole.set(this.role);

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

    public String getRefreshToken() { return refreshToken; }
    public String getIdToken()      { return idToken;      }


    public String getCustomUuid()   { return customUuid;   }
    public String getCustomRole()  { return role;    }

    public StringProperty  displayNameProperty()   { return displayName;   }
    public StringProperty  displayRoleProperty() { return displayRole; }

    private void inizializzaFlussoNotifiche() {
        try {
            ApiClient apiClient = new ApiClient("http://52.211.223.99");
            ObjectMapper mapper = new ObjectMapper();

            NotificationApiService apiService = new NotificationApiService(apiClient);
            this.notificationService = new NotificationService(apiService, mapper);
            this.notificationService.startNotificationListener(this.customUuid, this.idToken);
        } catch (Exception e) {
            System.err.println("AuthSession: Impossibile avviare il servizio notifiche.");
            e.printStackTrace();
        }
    }

    public NotificationService getNotificationService() {
        return notificationService;
    }

    public void clear() {
        email = "";
        role="";
        customUuid = "";
        authenticated.set(false);
        displayName.set("");
        displayRole.set("");
        notificationService=null;
    }
}