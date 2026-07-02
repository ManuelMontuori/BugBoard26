package org.frontend.viewsControllers;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.frontend.controllers.IssueController;
import org.frontend.models.Issue;
import org.frontend.services.AuthSession;
import org.frontend.util.DialogUtils;

import java.util.List;

public class HomeDashboardController {

    @FXML private TableView<Issue> tblIssueRecenti;
    @FXML private TableColumn<Issue, String> colTitle;
    @FXML private TableColumn<Issue, String> colType;
    @FXML private TableColumn<Issue, String> colPriority;
    @FXML private TableColumn<Issue, String> colState;
    @FXML private TableColumn<Issue, String> colCreatedAt;

    @FXML private Label labelWelcome;
    @FXML private Label labelRole;
    @FXML private Label labelTodo;
    @FXML private Label labelInProgress;
    @FXML private Label labelDone;

    private IssueController issueController;

    @FXML
    public void initialize() {
        try {
            String userUuid = AuthSession.getInstance().getCustomUuid();
            if (userUuid == null || userUuid.isBlank()) {
                throw new IllegalStateException("Utente non riconosciuto.");
            }

            labelWelcome.textProperty().bind(AuthSession.getInstance().displayNameProperty());
            labelRole.textProperty().bind(AuthSession.getInstance().displayRoleProperty());

            configuraColonneTabella();

        } catch (IllegalStateException e) {
            DialogUtils.showError("Errore di sessione",
                    "Utente non trovato.",
                    "Impossibile caricare i dati");
        }
    }

    public void initDependencies(IssueController issueController) {
        this.issueController = issueController;

        // Ora che l'issueController esiste, possiamo usarlo in sicurezza!
        tblIssueRecenti.setItems(this.issueController.getIssues());

        this.issueController.getIssues().addListener(new ListChangeListener<Issue>() {
            @Override
            public void onChanged(Change<? extends Issue> change) {
                aggiornaContatoriUI(HomeDashboardController.this.issueController.getIssues());
            }
        });

        String userUuid = AuthSession.getInstance().getCustomUuid();
        Platform.runLater(() -> {
            caricaDatiDashboard(userUuid);
        });
    }

    private void configuraColonneTabella() {
        colTitle.setCellValueFactory(data -> data.getValue().titleProperty());
        colCreatedAt.setCellValueFactory(data -> data.getValue().createdAtProperty().asString());

        colType.setCellValueFactory(data -> data.getValue().typeProperty());
        colType.setCellFactory(col -> creaBadgeCell());

        colPriority.setCellValueFactory(data -> data.getValue().priorityProperty());
        colPriority.setCellFactory(col -> creaBadgeCell());

        colState.setCellValueFactory(data -> data.getValue().statusProperty());
        colState.setCellFactory(col -> creaBadgeCell());
    }

    private TableCell<Issue, String> creaBadgeCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setGraphic(null); return; }
                Label badge = new Label(val);
                String normalized = val.toLowerCase().replace("_", "-");
                badge.getStyleClass().addAll("badge", "badge-" + normalized);
                setGraphic(badge);
                setText(null);
            }
        };
    }

    private void caricaDatiDashboard(String userUuid) {
        try {
            issueController.loadMyIssues(userUuid);
        } catch (Exception e) {
            e.printStackTrace();
            DialogUtils.showError("Errore di Connessione",
                    "Impossibile caricare i dati. Assicurati che il backend sia attivo.",
                    "Server non raggiungibile");
        }
    }

    private void aggiornaContatoriUI(List<Issue> list) {
        Integer todoCount=0;
        Integer doneCount=0;
        Integer inProgressCount=0;
        for (Issue issue : list) {
            if(issue.getStatus().equalsIgnoreCase("TODO"))
                todoCount++;
            else if(issue.getStatus().equalsIgnoreCase("DONE"))
                doneCount++;
            else if(issue.getStatus().equalsIgnoreCase("IN_PROGRESS"))
                inProgressCount++;
        }

        final int finalTodo = todoCount;
        final int finalInProgress = inProgressCount;
        final int finalDone = doneCount;

        Platform.runLater(() -> {
            if (labelTodo != null) labelTodo.setText(String.valueOf(finalTodo));
            if (labelInProgress != null) labelInProgress.setText(String.valueOf(finalInProgress));
            if (labelDone != null) labelDone.setText(String.valueOf(finalDone));
        });
    }
}