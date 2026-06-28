package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.frontend.controllers.IssueController;
import org.frontend.models.Issue;
import org.frontend.services.AuthSession;

public class DashboardViewController {

    @FXML private TableView<Issue> tblIssueRecenti;
    @FXML private TableColumn<Issue, String> colTitle;
    @FXML private TableColumn<Issue, String> colType;
    @FXML private TableColumn<Issue, String> colPriority;
    @FXML private TableColumn<Issue, String> colState;

    // DIP: Rimosso il 'new' e reso configurabile dall'esterno
    private IssueController issueController;

    // ════════════════════════════════════════════════════════════════════════
    // METODO DI INIEZIONE DELLE DIPENDENZE (DI)
    // ════════════════════════════════════════════════════════════════════════
    public void initDependencies(IssueController issueController) {
        this.issueController = issueController;

        // Colleghiamo la tabella alla lista del controller logico e scarichiamo i dati
        tblIssueRecenti.setItems(issueController.getIssues());
        caricaDatiDashboard();
    }

    @FXML
    public void initialize() {
        // Prepariamo solo lo scheletro visivo della tabella
        configuraColonneTabella();
    }

    /**
     * Mappa le proprietà dell'oggetto Issue sulle colonne della tabella.
     */
    private void configuraColonneTabella() {
        colTitle.setCellValueFactory(data -> data.getValue().titleProperty());
        colType.setCellValueFactory(data -> data.getValue().typeProperty());
        colPriority.setCellValueFactory(data -> data.getValue().priorityProperty());
        colState.setCellValueFactory(data -> data.getValue().statusProperty());
    }

    /**
     * Recupera l'UUID dell'utente e ordina al controller di caricare i dati dal server.
     */
    private void caricaDatiDashboard() {
        if (issueController == null) return; // Sicurezza

        // Recuperiamo l'ID dell'utente loggato
        String userUuid = AuthSession.getInstance().getCustomUuid();

        if (userUuid != null && !userUuid.isBlank()) {
            issueController.loadMyIssues(userUuid);
        } else {
            System.err.println("Attenzione: UUID utente mancante. Impossibile caricare la dashboard.");
        }
    }
}