package org.frontend.views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.frontend.services.Auth.CallbackServer;
import org.frontend.services.Auth.CognitoAuthService;

import java.awt.Desktop;
import java.net.URI;

public class WelcomeViewController {

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            CallbackServer callbackServer = new CallbackServer();
            callbackServer.start();
            String loginUrl = CognitoAuthService.buildHostedUiLoginUrl();

            // apre il browser di default
            Desktop.getDesktop().browse(new URI(loginUrl));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}