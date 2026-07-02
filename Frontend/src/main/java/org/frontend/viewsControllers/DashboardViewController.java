package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.frontend.controllers.IssueController;
import org.frontend.models.Issue;
import org.frontend.services.AuthSession;

public class DashboardViewController {

    @FXML
    private TableView<Issue> tblIssueRecenti;
    @FXML
    private TableColumn<Issue, String> colTitle;
    @FXML
    private TableColumn<Issue, String> colType;
    @FXML
    private TableColumn<Issue, String> colPriority;
    @FXML
    private TableColumn<Issue, String> colState;

    private IssueController issueController;

    public void initDependencies(IssueController issueController) {
        this.issueController = issueController;

        tblIssueRecenti.setItems(issueController.getIssues());
        caricaDatiDashboard();
    }

    @FXML
    public void initialize() {
        configuraColonneTabella();
    }

    private void configuraColonneTabella() {
        colTitle.setCellValueFactory(data -> data.getValue().titleProperty());
        colType.setCellValueFactory(data -> data.getValue().typeProperty());
        colPriority.setCellValueFactory(data -> data.getValue().priorityProperty());
        colState.setCellValueFactory(data -> data.getValue().statusProperty());
    }

    private void caricaDatiDashboard() {
        if (issueController == null)
            return;

        String userUuid = AuthSession.getInstance().getCustomUuid();

        if (userUuid != null && !userUuid.isBlank()) {
            issueController.loadMyIssues(userUuid);
        } else {
            System.err.println("Attenzione: UUID utente mancante. Impossibile caricare la dashboard.");
        }
    }
}