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
    private Button btnDashboard;
    @FXML
    private BorderPane mainBorderPane;

    @FXML
    public void initialize() {
       loadSubPage("/org/frontend/view/home-dashboard.fxml");
    }

    @FXML
    public void handleButtonAction(ActionEvent event) {
        loadSubPage("/org/frontend/view/home-dashboard.fxml");
    }
    
    // Metodo riutilizzabile per evitare duplicazione di codice
    private void loadSubPage(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent view = loader.load();
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore: impossibile caricare la vista " + path);
        }
    }

}
