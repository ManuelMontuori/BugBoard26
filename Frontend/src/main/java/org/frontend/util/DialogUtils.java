package org.frontend.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;

public class DialogUtils {
    public static void mostraErroreConnessione() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di Connessione");
            alert.setHeaderText("Server non raggiungibile");
            alert.setContentText("Impossibile caricare i dati. Assicurati che il backend sia attivo.");
            alert.showAndWait();
        });
    }
}