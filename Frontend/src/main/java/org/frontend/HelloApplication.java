package org.frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.frontend.services.Auth.CallbackServer;
import org.frontend.services.Auth.CognitoAuthService;
import org.frontend.services.Auth.LoginEvent;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        CallbackServer callbackServer = new CallbackServer();
        callbackServer.start();

        // IMPORTANTISSIMO: genera PKCE e stampa il link da aprire a mano
        String loginUrl = CognitoAuthService.buildHostedUiLoginUrl();
        System.out.println("Apri questo URL nel browser:\n" + loginUrl);


        // il resto della UI
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/frontend/view/welcome-view.fxml"));
        // Carica la scena home (già pronta in memoria, non ancora mostrata)


        FXMLLoader dashboardLoader = new FXMLLoader(getClass().getResource("/org/frontend/view/dashboard-view.fxml"));
        Scene dashboardScene = new Scene(dashboardLoader.load());

        LoginEvent.addListener(() -> {
            // Qui sei sull'FX thread: cambia scena, aggiorna label, ecc.
            stage.setScene(dashboardScene);
        });

        Scene scene = new Scene(fxmlLoader.load(), 478, 420);
        stage.setScene(scene);
        stage.show();
    }

}
