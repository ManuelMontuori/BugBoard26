package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.frontend.controllers.IssueController;
import org.frontend.models.Issue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IssueListController {

    // 1. Definizione della Tabella e delle colonne
    @FXML private TableView<Issue> tblAllIssues;
    @FXML private TableColumn<Issue, String> colUuid;
    @FXML private TableColumn<Issue, String> colTitle;
    @FXML private TableColumn<Issue, String> colDescription;
    @FXML private TableColumn<Issue, String> colType;
    @FXML private TableColumn<Issue, String> colPriority;
    @FXML private TableColumn<Issue, String> colStatus;
    @FXML private TableColumn<Issue, LocalDateTime> colCreatedAt;
    @FXML private TableColumn<Issue, LocalDateTime> colResolvedAt;

    // 2. Componenti per il Pannello Dettagli Laterale (Slide-out)
    @FXML private VBox pnlDetails;
    @FXML private javafx.scene.control.Label lblDetTitle;
    @FXML private javafx.scene.control.Label lblDetDescription;
    @FXML private javafx.scene.control.Label lblDetStatus;
    @FXML private javafx.scene.control.Label lblDetType;
    @FXML private javafx.scene.control.Label lblDetPriority;
    @FXML private javafx.scene.control.Label lblDetUuid;

    // 3. Componenti per la Barra di Ricerca
    @FXML private TextField txtSearch;

    private final IssueController issueController = new IssueController();

    // Formatter per mostrare le date in modo leggibile (es: 23/06/2026 11:12)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @FXML
    public void initialize() {
        configuraColonneTabella();

        // Abilita il menu contestuale "Copia" sulle Label statiche del pannello dettagli
        configuraCopiaSuLabel(lblDetUuid);
        configuraCopiaSuLabel(lblDetTitle);
        configuraCopiaSuLabel(lblDetDescription);

        // Listener sulla selezione delle righe: quando l'utente clicca una issue, mostra i dettagli
        tblAllIssues.getSelectionModel().selectedItemProperty().addListener((obs, vecchiaSelezione, nuovaSelezione) -> {
            if (nuovaSelezione != null) {
                mostraDettagli(nuovaSelezione);
            }
        });

        // Opzionale: se preferisci una ricerca "real-time" mentre scrivi, scommenta le righe sotto:
        /*
        txtSearch.textProperty().addListener((observable, vecchiaParola, nuovaParola) -> {
            gestisciRicercaDinamica(nuovaParola);
        });
        */

        // Colleghiamo la tabella alla lista di QUESTA istanza
        tblAllIssues.setItems(issueController.getIssues());

        // Ordiniamo di scaricare i dati iniziali (senza filtri)
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

        // Campi ENUM/Badge
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
     * Azione associata al bottone "Cerca". Invia la keyword al controller di logica.
     */
    @FXML
    private void gestisciRicerca() {
        String keyword = txtSearch.getText();
        if (keyword == null || keyword.isBlank()) {
            caricaTutteLeIssue(); // Se vuoto, ricarica tutto
            return;
        }

        try {
            // Supponendo che il tuo issueController abbia un metodo del genere per invocare /search?keyword=...
            issueController.searchIssue(keyword.trim());
            chiudiDettagli(); // Chiude eventuali dettagli aperti per evitare disallineamenti dati
        } catch (Exception e) {
            System.err.println("Errore durante la ricerca.");
            e.printStackTrace();
        }
    }

    /**
     * Azione associata al bottone "Annulla". Svuota il campo e ricarica i dati completi.
     */
    @FXML
    private void resetRicerca() {
        txtSearch.clear();
        caricaTutteLeIssue();
        chiudiDettagli();
    }

    /**
     * Popola il pannello di destra con i dati della issue selezionata e lo rende visibile.
     */
    private void mostraDettagli(Issue issue) {
        lblDetUuid.setText(issue.getUuid());
        lblDetTitle.setText(issue.getTitle());
        lblDetDescription.setText(issue.getDescription() != null && !issue.getDescription().isBlank()
                ? issue.getDescription()
                : "Nessuna descrizione presente.");

        // 1. Gestione STATO nel pannello
        lblDetStatus.setText(issue.getStatus());
        lblDetStatus.getStyleClass().clear();
        if (issue.getStatus() != null) {
            String normalizedStatus = issue.getStatus().toLowerCase().replace("_", "-");
            lblDetStatus.getStyleClass().addAll("label", "badge", "badge-" + normalizedStatus);
        }

        // 2. Gestione TIPO nel pannello
        lblDetType.setText(issue.getType());
        lblDetType.getStyleClass().clear();
        if (issue.getType() != null) {
            String normalizedType = issue.getType().toLowerCase().replace("_", "-");
            lblDetType.getStyleClass().addAll("label", "badge", "badge-" + normalizedType);
        }

        // 3. Gestione PRIORITÀ nel pannello
        lblDetPriority.setText(issue.getPriority());
        lblDetPriority.getStyleClass().clear();
        if (issue.getPriority() != null) {
            String normalizedPriority = issue.getPriority().toLowerCase().replace("_", "-");
            lblDetPriority.getStyleClass().addAll("label", "badge", "badge-" + normalizedPriority);
        }

        // Rende il pannello visibile e abilita il suo calcolo dello spazio nel layout
        pnlDetails.setManaged(true);
        pnlDetails.setVisible(true);
    }

    /**
     * Azione collegata al bottone "X" per chiudere il pannello laterale.
     */
    @FXML
    private void chiudiDettagli() {
        pnlDetails.setVisible(false);
        pnlDetails.setManaged(false);
        // Pulisce la selezione così la riga non rimane evidenziata e si può ricliccare
        tblAllIssues.getSelectionModel().clearSelection();
    }

    /**
     * Helper per la generazione dei badge grafici CSS nella TableView (Type, Priority, Status).
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
     * Aggiunge un menu con tasto destro "Copia" su una Label tradizionale.
     */
    private void configuraCopiaSuLabel(javafx.scene.control.Label label) {
        javafx.scene.control.ContextMenu contextMenu = new javafx.scene.control.ContextMenu();
        javafx.scene.control.MenuItem copyMenu = new javafx.scene.control.MenuItem("Copia");

        copyMenu.setOnAction(e -> {
            if (label.getText() != null) {
                javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                content.putString(label.getText());
                javafx.scene.input.Clipboard.getSystemClipboard().setContent(content);
            }
        });

        contextMenu.getItems().add(copyMenu);
        label.setContextMenu(contextMenu);
        label.setStyle(label.getStyle() + "; -fx-cursor: hand;");
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