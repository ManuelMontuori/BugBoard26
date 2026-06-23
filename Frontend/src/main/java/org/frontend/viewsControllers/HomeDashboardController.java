package org.frontend.viewsControllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.frontend.controllers.IssueController;
import org.frontend.models.Issue;
import org.frontend.services.ApiClient;
import org.frontend.services.AuthSession;
import org.frontend.services.IssueApiService;
import org.frontend.services.IssueService;

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

    private IssueService issueService;

    @FXML
    public void initialize() {
        String userUuid = AuthSession.getInstance().getCustomUuid();
        if (userUuid == null || userUuid.isBlank()) {
            System.err.println("Impossibile caricare la dashboard: UUID utente mancante.");
            return;
        }

        ApiClient apiClient = new ApiClient("http://localhost:8080");
        IssueApiService apiService = new IssueApiService(apiClient);
        this.issueService = new IssueService(apiService);

        labelWelcome.textProperty().bind(AuthSession.getInstance().displayNameProperty());
        labelRole.textProperty().bind(AuthSession.getInstance().displayRoleProperty());

        configuraColonneTabella();

        caricaDatiDashboard(userUuid);
    }

    private void configuraColonneTabella() {
        colTitle.setCellValueFactory(data -> data.getValue().titleProperty());
        colCreatedAt.setCellValueFactory(data -> data.getValue().createdAtProperty().asString());

        // Celle personalizzate (Sintetizzate usando un metodo helper per evitare codice duplicato)
        colType.setCellValueFactory(data -> data.getValue().typeProperty());
        colType.setCellFactory(col -> creaBadgeCell());

        colPriority.setCellValueFactory(data -> data.getValue().priorityProperty());
        colPriority.setCellFactory(col -> creaBadgeCell());

        colState.setCellValueFactory(data -> data.getValue().statusProperty());
        colState.setCellFactory(col -> creaBadgeCell());
    }

    // Helper per non ripetere 3 volte lo stesso identico codice di updateItem
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

//            // prova manuale di createdIssue
//            IssueController controller=new IssueController();
//            Issue prova = new Issue();
//            prova.setTitle("Prova da frontend n 1");
//            prova.setDescription("Descrizione della prima prova dal frontend");
//            prova.setType("BUG");
//            prova.setPriority("HIGH");
//            controller.createIssue(prova);


            // Recupera le issue VERE usando l'issueService appena istanziato
            List<Issue> mieIssue = issueService.findAssignedToMe(userUuid);

            // Popola la tabella con i dati reali dell'utente
            tblIssueRecenti.setItems(FXCollections.observableArrayList(mieIssue));

            // Aggiorna i contatori calcolandoli in modo pulito tramite Stream API
            aggiornaContatoriUI(mieIssue);

        } catch (Exception e) {
            System.err.println("Errore durante il recupero delle issue dal server.");
            e.printStackTrace();
        }
    }

    /**
     * RICEVE I DATI GIÀ PRONTI. Il suo unico scopo è renderizzarli a schermo.
     */
    private void aggiornaContatoriUI(List<Issue> list) {
        Integer todoCount=0;
        Integer doneCount=0;
        Integer inProgressCount=0;
        for (Issue issue : list) {
            System.out.println(issue.getStatus());
            if(issue.getStatus().equalsIgnoreCase("TODO"))
                todoCount++;
            else if(issue.getStatus().equalsIgnoreCase("DONE"))
                doneCount++;
            else if(issue.getStatus().equalsIgnoreCase("IN_PROGRESS"))
                inProgressCount++;
            System.out.println(todoCount + " " + doneCount + " " + inProgressCount);
        }

        final int finalTodo = todoCount;
        final int finalInProgress = inProgressCount;
        final int finalDone = doneCount;

        // Aggiornamento grafico racchiuso su Platform.runLater per sicurezza sul thread JavaFX
        Platform.runLater(() -> {
            if (labelTodo != null) labelTodo.setText(String.valueOf(finalTodo));
            if (labelInProgress != null) labelInProgress.setText(String.valueOf(finalInProgress));
            if (labelDone != null) labelDone.setText(String.valueOf(finalDone));
        });
    }
}

