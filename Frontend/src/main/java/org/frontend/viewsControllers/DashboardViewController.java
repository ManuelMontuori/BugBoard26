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

    // 1. Creiamo una NUOVA istanza del controller dedicata solo a questa View
    private final IssueController controller = new IssueController();

    @FXML
    public void initialize() {
        // 2. Impostiamo il comportamento visivo delle colonne
        configuraColonneTabella();

        // 3. LA MAGIA DEL BINDING: Colleghiamo la tabella alla lista del controller
        tblIssueRecenti.setItems(controller.getIssues());

        // 4. Diamo l'ordine di scaricare i dati
        caricaDatiDashboard();
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
        // Recuperiamo l'ID dell'utente loggato
        String userUuid = AuthSession.getInstance().getCustomUuid();

        if (userUuid != null && !userUuid.isBlank()) {
            // Chiamiamo il nuovo metodo specifico per le issue dell'utente
            controller.loadMyIssues(userUuid);
        } else {
            System.err.println("Attenzione: UUID utente mancante. Impossibile caricare la dashboard.");

            // NOTA: Se invece questa schermata dovesse mostrare *tutte* le issue del sistema a prescindere dall'utente,
            // dovresti usare: controller.loadAllIssues();
        }
    }
}