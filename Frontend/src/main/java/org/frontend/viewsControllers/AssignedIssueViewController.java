package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.frontend.controllers.IssueController;
import org.frontend.controllers.UserController;
import org.frontend.models.Issue;
import org.frontend.models.UserWorkload;
import org.frontend.util.BadgeUtils;
import org.frontend.util.DialogUtils;
import org.frontend.util.IssueUI;
import org.frontend.util.WorkloadUI;

public class AssignedIssueViewController {

    @FXML private ComboBox<Issue> comboIssue;
    @FXML private VBox dettagliPane;
    @FXML private Label lblTitolo, lblDescrizione, lblTipo, lblPriorita, lblStato;
    @FXML private Label lblAiSuggerimento;
    @FXML private VBox listUtenti;
    @FXML private Button btnAssegna;

    private UserController userController;
    private IssueController issueController;
    private UserWorkload selectedUser;

    public void initDependencies(UserController userController, IssueController issueController) {
        this.userController = userController;
        this.issueController = issueController;
        caricaDatiIniziali();
    }

    @FXML
    public void initialize() {
        // Deleghiamo la formattazione visiva della ComboBox a una Factory dedicata
        comboIssue.setCellFactory(lv -> IssueUI.createIssueListCell());
        comboIssue.setButtonCell(IssueUI.createIssueListCell());

        comboIssue.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) aggiornaDettagli(newVal);
        });
    }

    private void aggiornaDettagli(Issue issue) {
        if (issue == null) {
            dettagliPane.setVisible(false);
            dettagliPane.setManaged(false);
            return;
        }

        lblTitolo.setText(issue.getTitle() != null ? issue.getTitle() : "Nessun Titolo");
        lblDescrizione.setText(issue.getDescription() != null ? issue.getDescription() : "Nessuna Descrizione");

        // Utilizziamo BadgeUtils per mantenere coerenza grafica in tutta l'app
        BadgeUtils.applyBadgeStyle(lblTipo, issue.getType());
        BadgeUtils.applyBadgeStyle(lblPriorita, issue.getPriority());
        BadgeUtils.applyBadgeStyle(lblStato, issue.getStatus());

        dettagliPane.setVisible(true);
        dettagliPane.setManaged(true);
    }

    private void caricaDatiIniziali() {
        if (issueController == null || userController == null) return;

        try {
            issueController.loadAllIssues();
            comboIssue.setItems(issueController.getIssues().filtered(i -> "TODO".equals(i.getStatus())));
            caricaUtenti();
        } catch (Exception e) {
            DialogUtils.mostraErrore("Errore", "Impossibile caricare i dati iniziali.", e.getMessage());
        }
    }

    private void caricaUtenti() {
        userController.loadWorkload();
        UserWorkload suggerito = userController.getSuggerito();

        if (suggerito != null) {
            lblAiSuggerimento.setText("Suggerimento: " + suggerito.getFullName() +
                    " ha il carico più basso (" + suggerito.getIssuesCount() + " issue)");
        }

        long max = userController.getWorkload().stream().mapToLong(UserWorkload::getIssuesCount).max().orElse(1);
        listUtenti.getChildren().clear();

        for (UserWorkload u : userController.getWorkload()) {
            // Deleghiamo la costruzione della riga a WorkloadUI
            HBox row = WorkloadUI.createWorkloadRow(u, max, u == suggerito);

            row.setOnMouseClicked(e -> {
                selectedUser = u;
                listUtenti.getChildren().forEach(n -> n.getStyleClass().remove("user-workload-row-selected"));
                row.getStyleClass().add("user-workload-row-selected");
            });

            listUtenti.getChildren().add(row);
        }
    }

    @FXML
    private void onAssegnaClicked() {
        Issue selectedIssue = comboIssue.getSelectionModel().getSelectedItem();

        if (selectedIssue == null || selectedUser == null) {
            DialogUtils.mostraErrore("Selezione incompleta", "Seleziona sia una issue che un utente.", "");
            return;
        }

        try {
            userController.assignIssue(selectedIssue.getUuid(), selectedUser.getUuid());
            DialogUtils.mostraInformazione("Successo", "Issue assegnata correttamente.");

            // Reset della UI dopo l'azione
            comboIssue.getSelectionModel().clearSelection();
            dettagliPane.setVisible(false);
            dettagliPane.setManaged(false);
            selectedUser = null;
            caricaUtenti();
        } catch (Exception e) {
            DialogUtils.mostraErrore("Errore", "Impossibile completare l'assegnazione.", e.getMessage());
        }
    }

    @FXML
    private void onAnnullaClicked() {
        // Logica di navigazione qui
    }
}