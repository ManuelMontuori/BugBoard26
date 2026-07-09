package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import org.frontend.controllers.IssueController;
import org.frontend.models.Issue;
import org.frontend.util.BadgeUtils;
import org.frontend.util.DateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IssueListController {

    @FXML private TableView<Issue> tblAllIssues;
    @FXML private TableColumn<Issue, String> colUuid;
    @FXML private TableColumn<Issue, String> colTitle;
    @FXML private TableColumn<Issue, String> colDescription;
    @FXML private TableColumn<Issue, String> colType;
    @FXML private TableColumn<Issue, String> colPriority;
    @FXML private TableColumn<Issue, String> colStatus;
    @FXML private TableColumn<Issue, LocalDateTime> colCreatedAt;
    @FXML private TableColumn<Issue, LocalDateTime> colResolvedAt;

    @FXML private VBox pnlDetails;
    @FXML private Label lblDetTitle;
    @FXML private Label lblDetDescription;
    @FXML private Label lblDetStatus;
    @FXML private Label lblDetType;
    @FXML private Label lblDetPriority;
    @FXML private Label lblDetUuid;

    @FXML private TextField txtSearch;

    @FXML private ComboBox<String> cmbStatus;
    @FXML private ComboBox<String> cmbPriority;
    @FXML private ComboBox<String> cmbType;

    private IssueController issueController;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public void initDependencies(IssueController issueController) {
        this.issueController = issueController;
        tblAllIssues.setItems(issueController.getIssues());
        caricaTutteLeIssue();
    }

    @FXML
    public void initialize() {
        configuraColonneTabella();
        popolaFiltriComboBox();

        tblAllIssues.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) mostraDettagli(newVal);
        });
    }

    private void configuraColonneTabella() {
        colUuid.setCellValueFactory(data -> data.getValue().uuidProperty());
        colTitle.setCellValueFactory(data -> data.getValue().titleProperty());
        colDescription.setCellValueFactory(data -> data.getValue().descriptionProperty());

        colType.setCellValueFactory(data -> data.getValue().typeProperty());
        colType.setCellFactory(col -> BadgeUtils.createBadgeCell());

        colPriority.setCellValueFactory(data -> data.getValue().priorityProperty());
        colPriority.setCellFactory(col -> BadgeUtils.createBadgeCell());

        colStatus.setCellValueFactory(data -> data.getValue().statusProperty());
        colStatus.setCellFactory(col -> BadgeUtils.createBadgeCell());

        colCreatedAt.setCellValueFactory(data -> data.getValue().createdAtProperty());
        colCreatedAt.setCellFactory(col -> DateUtils.createDateCell(DATE_FORMATTER));

        colResolvedAt.setCellValueFactory(data -> data.getValue().resolvedAtProperty());
        colResolvedAt.setCellFactory(col -> DateUtils.createDateCell(DATE_FORMATTER));
    }

    private void popolaFiltriComboBox() {
        cmbStatus.getItems().addAll("TODO", "IN_PROGRESS", "DONE");
        cmbPriority.getItems().addAll("HIGH", "MEDIUM", "LOW");
        cmbType.getItems().addAll("BUG", "FEATURE", "DOCUMENTATION", "QUESTION");
    }

    @FXML
    private void gestisciRicerca() {
        if (issueController == null) return;

        String keyword = txtSearch.getText();

        // Se c'è del testo inserito nella barra di ricerca, usiamo la ricerca testuale
        if (keyword != null && !keyword.isBlank()) {
            issueController.searchIssue(keyword.trim());
        } else {
            caricaTutteLeIssue();
        }
        chiudiDettagli();
    }

    @FXML
    private void resetRicerca() {
        txtSearch.clear();
        cmbStatus.setValue(null);
        cmbPriority.setValue(null);
        cmbType.setValue(null);

        caricaTutteLeIssue();
        chiudiDettagli();
    }

    private void mostraDettagli(Issue issue) {
        lblDetUuid.setText(issue.getUuid());
        lblDetTitle.setText(issue.getTitle());
        lblDetDescription.setText(issue.getDescription() != null && !issue.getDescription().isBlank()
                ? issue.getDescription() : "Nessuna descrizione presente.");

        lblDetStatus.setText(issue.getStatus());
        BadgeUtils.applyBadgeStyle(lblDetStatus, issue.getStatus());

        lblDetType.setText(issue.getType());
        BadgeUtils.applyBadgeStyle(lblDetType, issue.getType());

        lblDetPriority.setText(issue.getPriority());
        BadgeUtils.applyBadgeStyle(lblDetPriority, issue.getPriority());

        pnlDetails.setManaged(true);
        pnlDetails.setVisible(true);
    }

    @FXML
    private void chiudiDettagli() {
        pnlDetails.setVisible(false);
        pnlDetails.setManaged(false);
        tblAllIssues.getSelectionModel().clearSelection();
    }

    private void caricaTutteLeIssue() {
        if (issueController != null) {
            String status = cmbStatus.getValue();
            String priority = cmbPriority.getValue();
            String type = cmbType.getValue();

            issueController.loadAllIssues(status, priority, type);
        }
    }
}