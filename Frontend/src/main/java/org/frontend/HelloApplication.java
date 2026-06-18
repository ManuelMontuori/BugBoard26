package org.frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.frontend.services.Auth.CallbackServer;
import org.frontend.services.Auth.CognitoAuthService;
import org.frontend.services.Auth.LoginEvent;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        CallbackServer callbackServer = new CallbackServer();
        callbackServer.start();

        // IMPORTISSIMO: genera PKCE e stampa il link da aprire a mano
        String loginUrl = CognitoAuthService.buildHostedUiLoginUrl();
        System.out.println("Apri questo URL nel browser:\n" + loginUrl);

        // 1. Carica SOLO il modulo di benvenuto/login all'avvio
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/frontend/view/welcome-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 546, 400);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UNDECORATED); // Login senza barra superiore

        // 2. Rimani in ascolto dell'evento di login effettuato
        LoginEvent.addListener(() -> {
            // Usiamo Platform.runLater per essere sicuri di manipolare l'interfaccia sul thread corretto
            Platform.runLater(() -> {
                try {
                    // Chiudiamo lo stage del login (undecorated)
                    stage.close();

                    // ORA E SOLO ORA carichiamo la dashboard (i token ora ci sono!)
                    FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/org/frontend/view/dashboard.fxml"));
                    Scene dashboardScene = new Scene(dashboardLoader.load());

                    // Creiamo il nuovo stage per l'applicazione principale (con la barra di sistema)
                    Stage mainStage = new Stage();
                    mainStage.setScene(dashboardScene);
                    mainStage.setMinWidth(800);
                    mainStage.setMinHeight(600);
                    mainStage.centerOnScreen();
                    mainStage.setResizable(true);

                    mainStage.show();
                } catch (Exception e) {
                    System.err.println("Errore durante il caricamento della dashboard post-login:");
                    e.printStackTrace();
                }
            });
        });

        // Mostra la schermata di login
        stage.show();
    }
}