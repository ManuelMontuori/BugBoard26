package org.frontend.viewsControllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.frontend.controllers.DashboardController;
import org.frontend.models.Issue;
import org.frontend.services.ApiClient;
import org.frontend.services.AuthSession;
import org.frontend.services.IssueApiService;
import org.frontend.services.IssueService;

import java.util.List;

public class HomeDashboardController {

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

    @FXML
    private TableColumn<Issue, String> colCreatedAt;

    @FXML
    private Label labelWelcome;

    @FXML
    private Label labelRole;

    // --- Contatori grafici per gli stati ---
    @FXML
    private Label labelTodo;

    @FXML
    private Label labelInProgress;

    @FXML
    private Label labelDone;

    private DashboardController controller;
    private IssueService issueService;

    @FXML
    public void initialize() {
        // 1. Inizializzazione dei controller e dei servizi dedicati alle API
        controller = new DashboardController();

        ApiClient apiClient = new ApiClient("http://localhost:8080");
        IssueApiService apiService = new IssueApiService(apiClient);
        this.issueService = new IssueService(apiService);

        // 2. Data Binding per i dati dell'utente
        labelWelcome.textProperty().bind(AuthSession.getInstance().displayNameProperty());
        labelRole.textProperty().bind(AuthSession.getInstance().displayRoleProperty());

        // 3. Configurazione delle colonne della Tabella (Mappatura properties)
        colTitle.setCellValueFactory(data -> data.getValue().titleProperty());
        colCreatedAt.setCellValueFactory(data -> data.getValue().createdAtProperty().asString());

        // Configurazione delle celle con i Badge grafici per il Tipo
        colType.setCellValueFactory(data -> data.getValue().typeProperty());
        colType.setCellFactory(col -> new TableCell<>() {
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
        });

        // Configurazione delle celle con i Badge grafici per la Priorità
        colPriority.setCellValueFactory(data -> data.getValue().priorityProperty());
        colPriority.setCellFactory(col -> new TableCell<>() {
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
        });

        // Configurazione delle celle con i Badge grafici per lo Stato
        colState.setCellValueFactory(data -> data.getValue().statusProperty());
        colState.setCellFactory(col -> new TableCell<>() {
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
        });

        // 4. Caricamento della Tabella (Tutte le issue)
        String userUuid = AuthSession.getInstance().getCustomUuid();
        controller.loadMyIssues(userUuid);
        tblIssueRecenti.setItems(controller.getIssues());

        // 5. Caricamento dei contatori personali (Issue assegnate)
        loadPersonalContatori();
    }

    /**
     * Recupera lo UUID da Cognito e interroga l'API per ottenere le issue assegnate all'utente
     */
    private void loadPersonalContatori() {
        String userUuid = AuthSession.getInstance().getCustomUuid();
        System.out.println("🚨 STAMPA UUID INVIATO AL BACKEND: [" + userUuid + "]");

        if (userUuid == null || userUuid.isBlank()) {
            System.err.println("Impossibile caricare i contatori personali: UUID mancante in sessione.");
            return;
        }

        // Chiamata all'API tramite IssueService
        List<Issue> assignedIssues = issueService.findAssignedToMe(userUuid);

        // Aggiorna la grafica in sicurezza sul thread JavaFX
        Platform.runLater(() -> aggiornaContatoriUI(assignedIssues));
    }

    /**
     * Esegue il conteggio filtrato per stato e imposta i testi nei Label corrispondenti
     */
    private void aggiornaContatoriUI(List<Issue> list) {
        if (list == null) return;


        long todoCount = list.stream()
                .filter(i -> "TODO".equalsIgnoreCase(i.getStatus()))
                .count();

        long inProgressCount = list.stream()
                .filter(i -> "IN_PROGRESS".equalsIgnoreCase(i.getStatus()))
                .count();

        long doneCount = list.stream()
                .filter(i -> "DONE".equalsIgnoreCase(i.getStatus()))
                .count();

        // Evita potenziali NullPointerException impostando i valori solo se i componenti FXML esistono
        if (labelTodo != null) labelTodo.setText(String.valueOf(todoCount));
        if (labelInProgress != null) labelInProgress.setText(String.valueOf(inProgressCount));
        if (labelDone != null) labelDone.setText(String.valueOf(doneCount));
    }
}