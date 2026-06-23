package org.frontend.viewsControllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.frontend.HelloApplication;
import org.frontend.services.Auth.CallbackServer;
import org.frontend.services.Auth.CognitoAuthService;
import org.frontend.services.AuthSession;
import org.frontend.util.DialogUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class DashboardController {
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnSegnalaIssue;
    @FXML
    private Button btnElencoIssue;
    @FXML
    private BorderPane mainBorderPane;
    @FXML private VBox adminSection;
    @FXML private Button btnLogout;

    @FXML
    public void initialize() {
        String userRole = AuthSession.getInstance().getCustomRole(); // Suppongo restituisca "ADMIN", "USER", ecc.
        boolean isAdmin = "ADMIN".equalsIgnoreCase(userRole);

        if (isAdmin) {
            adminSection.setVisible(true);
            adminSection.setManaged(true);
        }
        loadSubPage("/org/frontend/view/home-dashboard.fxml");
    }


    @FXML
    public void handleButtonDashboard(ActionEvent event) {
        loadSubPage("/org/frontend/view/home-dashboard.fxml");
    }

    @FXML
    public void handleButtonElencoIssue(ActionEvent event) {
        loadSubPage("/org/frontend/view/home-elencoIssue.fxml");
    }

    @FXML
    public void handleButtonSegnalaIssue(ActionEvent event) {
        loadSubPage("/org/frontend/view/home-createIssue.fxml");
    }

    @FXML
    private void handleLogout() {
        try {
            // 1. Uccide il token sul server AWS e pulisce la sessione locale
            // (Chiama il metodo eccellente che avevi già scritto tu)
            CognitoAuthService.logout();

            // 2. Genera l'URL per pulire i cookie del browser
            String logoutUrl = CognitoAuthService.buildHostedUiLogoutUrl();

            // 3. Accende il CallbackServer sulla 9090 in attesa del ritorno da AWS
            CallbackServer callbackServer = new CallbackServer();
            callbackServer.start();

            // 4. Apre il browser per distruggere la sessione sul cloud
            Desktop.getDesktop().browse(new URI(logoutUrl));

            // 5. Chiudi la finestra attuale della Dashboard
            Stage currentStage = (Stage) btnLogout.getScene().getWindow();
            currentStage.close();

            // 6. Riapri la schermata di Login (Welcome View)
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/frontend/view/welcome-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 546, 400);

            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.setResizable(false);
            loginStage.centerOnScreen();
            loginStage.initStyle(StageStyle.UNDECORATED);
            loginStage.show();

        } catch (Exception e) {
            System.err.println("Errore durante il logout:");
            e.printStackTrace();
        }
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
        catch (Exception e) {
            DialogUtils.mostraErroreConnessione();
        }
    }

}
