package org.frontend.services.Auth;

import javafx.application.Platform;
import java.util.ArrayList;
import java.util.List;

public final class LoginEvent {

    private static final List<Runnable> listeners = new ArrayList<>();

    public static void addListener(Runnable r) {
        listeners.add(r);
    }

    /** Chiamato da CognitoAuthService dopo exchangeCode riuscito. */
    public static void fire() {
        // Siamo già su FX thread grazie a Platform.runLater in exchangeCode
        listeners.forEach(Runnable::run);
    }

    private LoginEvent() {}
}