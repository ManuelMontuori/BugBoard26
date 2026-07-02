package org.frontend;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.frontend.services.Auth.LoginEvent;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader(
                HelloApplication.class.getResource("/org/frontend/view/welcome-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 546, 400);

        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.initStyle(StageStyle.UNDECORATED);

        LoginEvent.addListener(() -> {

            Platform.runLater(() -> {
                try {

                    stage.close();

                    FXMLLoader dashboardLoader = new FXMLLoader(
                            getClass().getResource("/org/frontend/view/dashboard.fxml"));
                    Scene dashboardScene = new Scene(dashboardLoader.load());

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

        stage.show();
    }

    @Override
    public void stop() {

        System.out.println("Chiusura dell'applicazione in corso... Pulizia risorse.");

        System.exit(0);
    }
}