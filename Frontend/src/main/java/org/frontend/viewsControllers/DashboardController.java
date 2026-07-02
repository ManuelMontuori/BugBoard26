package org.frontend.viewsControllers;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.frontend.HelloApplication;
import org.frontend.controllers.IssueController;
import org.frontend.controllers.UserController;
import org.frontend.models.Notification;
import org.frontend.services.Auth.CallbackServer;
import org.frontend.services.Auth.CognitoAuthService;
import org.frontend.services.AuthSession;
import org.frontend.services.ReportRowService;
import org.frontend.util.DialogUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.function.Consumer;

public class DashboardController {
    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnSegnalaIssue;
    @FXML
    private Button btnElencoIssue;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private VBox adminSection;
    @FXML
    private Button btnLogout;
    @FXML
    private Label lblBadge;

    @FXML
    public void initialize() {
        String userRole = AuthSession.getInstance().getCustomRole();
        boolean isAdmin = "ADMIN".equalsIgnoreCase(userRole);

        if (isAdmin) {
            adminSection.setVisible(true);
            adminSection.setManaged(true);
        }

        if (AuthSession.getInstance().getNotificationService() != null) {

            AuthSession.getInstance().getNotificationService().loadNotifications();

            ObservableList<Notification> listaNotifiche =
                    AuthSession.getInstance().getNotificationService().getNotifications();

            IntegerBinding notificheNonLetteContatore = Bindings.createIntegerBinding(
                    () -> (int) listaNotifiche.stream().filter(n -> !n.isRead()).count(),
                    listaNotifiche
            );

            lblBadge.textProperty().bind(notificheNonLetteContatore.asString());

            BooleanBinding haNotificheNonLette = notificheNonLetteContatore.greaterThan(0);
            lblBadge.visibleProperty().bind(haNotificheNonLette);
            lblBadge.managedProperty().bind(haNotificheNonLette);
        }

        loadSubPage("/org/frontend/view/home-dashboard.fxml", controller -> {
            if (controller instanceof DashboardViewController dashCtrl) {
                dashCtrl.initDependencies(new IssueController());
            }
        });
    }

    @FXML
    public void handleButtonDashboard(ActionEvent event) {
        loadSubPage("/org/frontend/view/home-dashboard.fxml", controller -> {
            if (controller instanceof DashboardViewController dashCtrl) {
                dashCtrl.initDependencies(new IssueController());
            }
        });
    }

    @FXML
    public void handleButtonElencoIssue(ActionEvent event) {
        loadSubPage("/org/frontend/view/home-elencoIssue.fxml", controller -> {
            if (controller instanceof IssueListController elencoCtrl) {
                elencoCtrl.initDependencies(new IssueController());
            }
        });
    }

    @FXML
    public void handleButtonSegnalaIssue(ActionEvent event) {
        loadSubPage("/org/frontend/view/home-createIssue.fxml", controller -> {
            if (controller instanceof CreateIssueViewController createCtrl) {
                createCtrl.initDependencies(new IssueController());
            }
        });
    }

    @FXML
    public void handleButtonGestioneUtenti(ActionEvent event) {
        loadSubPage("/org/frontend/view/home-creaUtente.fxml", controller -> {
            if (controller instanceof CreateUserViewController utenteCtrl) {
                utenteCtrl.initDependencies(new UserController());
            }
        });
    }

    @FXML
    public void handleButtonAssegnaIssue(ActionEvent event) {
        loadSubPage("/org/frontend/view/home-assegnaIssue.fxml", controller -> {
            if (controller instanceof AssignedIssueViewController assegnaCtrl) {
                assegnaCtrl.initDependencies(new UserController(), new IssueController());
            }
        });
    }

    @FXML
    public void handleButtonReport(ActionEvent event) {
        loadSubPage("/org/frontend/view/home-report.fxml", controller -> {
            if (controller instanceof ReportViewController reportCtrl) {
                reportCtrl.initDependencies(new UserController(), new ReportRowService());
            }
        });
    }

    @FXML
    public void handleButtonNotification(ActionEvent event) {
        loadSubPage("/org/frontend/view/home-notification.fxml", controller -> {
            if (controller instanceof NotificationViewController notifCtrl) {
                notifCtrl.initDependencies(AuthSession.getInstance().getNotificationService());
            }
        });
    }

    @FXML
    private void handleLogout() {
        try {
            // Chiama il metodo di logout del servizio Cognito per invalidare la sessione
            CognitoAuthService.logout();

            // Genera l'URL di logout per il browser
            String logoutUrl = CognitoAuthService.buildHostedUiLogoutUrl();

            // Avvia un server di callback locale per ricevere la conferma di logout
            CallbackServer callbackServer = new CallbackServer();
            callbackServer.start();

            // Apre il browser predefinito con l'URL di logout
            Desktop.getDesktop().browse(new URI(logoutUrl));

            // Chiude la finestra corrente del dashboard
            Stage currentStage = (Stage) btnLogout.getScene().getWindow();
            currentStage.close();

            // Riapri la finestra di login dopo il logout
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

    private void loadSubPage(String path, Consumer<Object> initializer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Parent view = loader.load();

            if (initializer != null && loader.getController() != null) {
                initializer.accept(loader.getController());
            }

            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore: impossibile caricare la vista " + path);
        } catch (Exception e) {
            DialogUtils.showError("Errore di Rete.",
                    "Impossibile connettersi al server.",
                    "Connessione non riuscita.");
        }
    }
}