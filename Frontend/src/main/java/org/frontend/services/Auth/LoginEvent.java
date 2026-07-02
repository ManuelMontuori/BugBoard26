package org.frontend.services.Auth;

import java.util.ArrayList;
import java.util.List;

public final class LoginEvent {

    private static final List<Runnable> listeners = new ArrayList<>();

    public static void addListener(Runnable r) {
        listeners.add(r);
    }

    public static void fire() {
        // Siamo già su FX thread grazie a Platform.runLater in exchangeCode
        listeners.forEach(Runnable::run);
    }

    private LoginEvent() {}
}