package org.frontend.viewsControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class DashboardController {
    @FXML
    private Button button1;
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    public void handleButtonAction(ActionEvent event) {
        try {
            // 2. Carica il file FXML della sottopagina
            // Nota: Il percorso parte dalla cartella "resources" del tuo progetto
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/frontend/view/home-dashboard.fxml"));
            Parent nuovaVista = loader.load();

            // 3. Spedisci la nuova vista direttamente al centro del BorderPane
            mainBorderPane.setCenter(nuovaVista);


        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore: impossibile caricare il file FXML della sottopagina.");
        }
    }

}
