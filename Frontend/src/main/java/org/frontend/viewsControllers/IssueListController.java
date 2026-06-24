package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.frontend.controllers.IssueController;
import org.frontend.models.Issue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IssueListController {

    // 1. Definizione della Tabella e di tutti gli 11 campi dell'IssueDTO
    @FXML private TableView<Issue> tblAllIssues;
    @FXML private TableColumn<Issue, String> colUuid;
    @FXML private TableColumn<Issue, String> colTitle;
    @FXML private TableColumn<Issue, String> colDescription;
    @FXML private TableColumn<Issue, String> colType;
    @FXML private TableColumn<Issue, String> colPriority;
    @FXML private TableColumn<Issue, String> colStatus;
    @FXML private TableColumn<Issue, LocalDateTime> colCreatedAt;
    @FXML private TableColumn<Issue, LocalDateTime> colResolvedAt;

    private final IssueController issueController = new IssueController();

    // Formatter per mostrare le date in modo leggibile (es: 23/06/2026 11:12)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        configuraColonneTabella();

        // Colleghiamo la tabella alla lista di QUESTA istanza
        tblAllIssues.setItems(issueController.getIssues());

        // Ordiniamo di scaricare i dati
        caricaTutteLeIssue();
    }

    /**
     * Collega le proprietà del modello di dominio Issue alle rispettive colonne grafiche.
     */
    private void configuraColonneTabella() {
        // Campi di testo standard
        colUuid.setCellValueFactory(data -> data.getValue().uuidProperty());
        colTitle.setCellValueFactory(data -> data.getValue().titleProperty());
        colDescription.setCellValueFactory(data -> data.getValue().descriptionProperty());


        // Campi ENUM/Badge (Riutilizzo della tua ottima logica condizionale)
        colType.setCellValueFactory(data -> data.getValue().typeProperty());
        colType.setCellFactory(col -> creaBadgeCell());

        colPriority.setCellValueFactory(data -> data.getValue().priorityProperty());
        colPriority.setCellFactory(col -> creaBadgeCell());

        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        colStatus.setCellFactory(col -> creaBadgeCell());

        // Campi Data (LocalDateTime) mappati con un formatter personalizzato
        colCreatedAt.setCellValueFactory(data -> data.getValue().createdAtProperty());
        colCreatedAt.setCellFactory(col -> creaDataCell());

        colResolvedAt.setCellValueFactory(data -> data.getValue().resolvedAtProperty());
        colResolvedAt.setCellFactory(col -> creaDataCell());
    }

    /**
     * Helper per la generazione dei badge grafici CSS (Type, Priority, Status).
     */
    private TableCell<Issue, String> creaBadgeCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                javafx.scene.control.Label badge = new javafx.scene.control.Label(val);
                String normalized = val.toLowerCase().replace("_", "-");
                badge.getStyleClass().addAll("badge", "badge-" + normalized);
                setGraphic(badge);
                setText(null);
            }
        };
    }

    /**
     * Helper per formattare gli oggetti LocalDateTime in stringhe "dd/MM/yyyy HH:mm".
     * Se la data è nulla (es: issue non ancora risolta), mostra un trattino.
     */
    private TableCell<Issue, LocalDateTime> creaDataCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("-");
                    setGraphic(null);
                } else {
                    setText(DATE_FORMATTER.format(item));
                    setGraphic(null);
                }
            }
        };
    }

    /**
     * Esegue la chiamata al server per recuperare l'intero parco Issue senza filtri utente.
     */
    private void caricaTutteLeIssue() {
        try {
            issueController.loadAllIssues();
        } catch (Exception e) {
            System.err.println("Errore di caricamento.");
            e.printStackTrace();
        }
    }
}