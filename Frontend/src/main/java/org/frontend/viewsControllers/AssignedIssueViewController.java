package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Pos;
import org.frontend.controllers.IssueController;
import org.frontend.controllers.UserController;
import org.frontend.models.Issue;
import org.frontend.models.UserWorkload;
import org.frontend.util.DialogUtils;

public class AssignedIssueViewController {

    // Card sinistra
    @FXML private ComboBox<Issue> comboIssue;
    @FXML private VBox            dettagliPane;
    @FXML private Label           lblTitolo;
    @FXML private Label           lblDescrizione;
    @FXML private Label           lblTipo;
    @FXML private Label           lblPriorita;
    @FXML private Label           lblStato;

    // Card destra
    @FXML private Label           lblAiSuggerimento;
    @FXML private VBox            listUtenti;
    @FXML private Button          btnAssegna;

    private final UserController  userController  = new UserController();
    private final IssueController issueController = new IssueController();
    private UserWorkload selectedUser;

    @FXML
    public void initialize() {

        // Come la ComboBox mostra le issue
        comboIssue.setCellFactory(lv -> new ListCell<>() {
            @Override
            protected void updateItem(Issue issue, boolean empty) {
                super.updateItem(issue, empty);
                if (empty || issue == null) { setText(null); return; }
                setText("#" + issue.getUuid().substring(0, 8) + " — " + issue.getTitle());
            }
        });
        comboIssue.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Issue issue, boolean empty) {
                super.updateItem(issue, empty);
                if (empty || issue == null) { setText(null); return; }
                setText("#" + issue.getUuid().substring(0, 8) + " — " + issue.getTitle());
            }
        });

        // Carica solo le issue TODO
        issueController.loadAllIssues();
        comboIssue.setItems(
                issueController.getIssues()
                        .filtered(i -> "TODO".equals(i.getStatus()))
        );

        // Listener selezione issue
        comboIssue.getSelectionModel().selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) aggiornaDettagli(newVal);
                });

        // Carica utenti e workload
        caricaUtenti();
    }

    private void aggiornaDettagli(Issue issue) {
        // 1. Controllo di sicurezza fondamentale se l'issue passata è nulla
        if (issue == null) {
            dettagliPane.setVisible(false);
            dettagliPane.setManaged(false);
            return;
        }

        lblTitolo.setText(issue.getTitle() != null ? issue.getTitle() : "Nessun Titolo");
        lblDescrizione.setText(issue.getDescription() != null ? issue.getDescription() : "Nessuna Descrizione");

        // 2. Controllo di sicurezza sul Tipo (evita il crash al toLowerCase)
        String tipo = issue.getType();
        if (tipo != null) {
            aggiornaLabelBadge(lblTipo, tipo, "badge-" + tipo.toLowerCase().replace("_", "-"));
        } else {
            aggiornaLabelBadge(lblTipo, "UNKNOWN", "badge-unknown");
        }

        // 3. Controllo di sicurezza sulla Priorità (evita il crash al toLowerCase)
        String priorita = issue.getPriority();
        if (priorita != null) {
            aggiornaLabelBadge(lblPriorita, priorita, "badge-" + priorita.toLowerCase());
        } else {
            aggiornaLabelBadge(lblPriorita, "UNKNOWN", "badge-unknown");
        }

        // 4. Controllo di sicurezza sullo Stato
        String stato = issue.getStatus();
        if (stato != null) {
            String cssStato = switch (stato) {
                case "TODO"        -> "badge-todo";
                case "IN_PROGRESS" -> "badge-in-progress";
                case "DONE"        -> "badge-resolved";
                default            -> "";
            };
            aggiornaLabelBadge(lblStato, stato, cssStato);
        } else {
            aggiornaLabelBadge(lblStato, "UNKNOWN", "");
        }

        dettagliPane.setVisible(true);
        dettagliPane.setManaged(true);
    }

    private void aggiornaLabelBadge(Label lbl, String testo, String cssClass) {
        lbl.setText(testo);
        lbl.getStyleClass().removeIf(c -> c.startsWith("badge-"));
        if (!cssClass.isEmpty()) lbl.getStyleClass().add(cssClass);
    }

    private void caricaUtenti() {
        userController.loadWorkload();

        UserWorkload suggerito = userController.getSuggerito();
        if (suggerito != null) {
            lblAiSuggerimento.setText(
                    "Suggerimento: " + suggerito.getFullName() +
                            " ha il carico più basso (" + suggerito.getIssuesCount() + " issue attive)"
            );
            selectedUser = suggerito;
        }

        long max = userController.getWorkload().stream()
                .mapToLong(UserWorkload::getIssuesCount)
                .max().orElse(1);

        listUtenti.getChildren().clear();

        for (UserWorkload u : userController.getWorkload()) {
            HBox row = creaRigaUtente(u, max, u == suggerito);
            listUtenti.getChildren().add(row);
        }
    }

    private HBox creaRigaUtente(UserWorkload u, long max, boolean consigliato) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("user-workload-row");
        if (consigliato) row.getStyleClass().add("user-workload-row-selected");

        Label avatar = new Label(iniziali(u.getFullName()));
        avatar.getStyleClass().addAll("avatar", "avatar-blue");

        Label nome = new Label(u.getFullName());
        nome.getStyleClass().add("fval");
        HBox.setHgrow(nome, Priority.ALWAYS);

        ProgressBar bar = new ProgressBar((double) u.getIssuesCount() / max);
        bar.setPrefWidth(80);
        bar.setPrefHeight(5);
        bar.getStyleClass().add(coloreBarra(u.getIssuesCount(), max));

        Label count = new Label(String.valueOf(u.getIssuesCount()));
        count.getStyleClass().add("label-muted");
        count.setMinWidth(24);

        row.getChildren().addAll(avatar, nome, bar, count);

        if (consigliato) {
            Label chip = new Label("consigliato");
            chip.getStyleClass().add("ai-chip");
            row.getChildren().add(chip);
        }

        row.setOnMouseClicked(e -> {
            selectedUser = u;
            listUtenti.getChildren().forEach(n ->
                    n.getStyleClass().remove("user-workload-row-selected"));
            row.getStyleClass().add("user-workload-row-selected");
        });

        return row;
    }

    @FXML
    private void onAssegnaClicked() {
        Issue selectedIssue = comboIssue.getSelectionModel().getSelectedItem();

        if (selectedIssue == null) {
            DialogUtils.mostraErrore("Nessuna issue selezionata",
                    "Seleziona una issue dal menu prima di procedere.", "");
            return;
        }
        if (selectedUser == null) {
            DialogUtils.mostraErrore("Nessun utente selezionato",
                    "Seleziona un membro del team prima di procedere.", "");
            return;
        }

        try {
            userController.assignIssue(selectedIssue.getUuid(), selectedUser.getUuid());
            DialogUtils.mostraInformazione("Issue assegnata",
                    "Issue assegnata a " + selectedUser.getFullName() + " con successo.");
            comboIssue.getSelectionModel().clearSelection();
            dettagliPane.setVisible(false);
            dettagliPane.setManaged(false);
            selectedUser = null;
            caricaUtenti();
        } catch (Exception e) {
            DialogUtils.mostraErrore("Errore", "Impossibile assegnare la issue.", "");
        }
    }

    @FXML
    private void onAnnullaClicked() {
        // MainController.getInstance().loadView("...");
    }

    private String iniziali(String fullName) {
        String[] parts = fullName.trim().split(" ");
        if (parts.length >= 2)
            return String.valueOf(parts[0].charAt(0)) + parts[1].charAt(0);
        return fullName.substring(0, Math.min(2, fullName.length())).toUpperCase();
    }

    private String coloreBarra(long count, long max) {
        double ratio = (double) count / max;
        if (ratio < 0.35) return "progress-bar-green";
        if (ratio < 0.70) return "progress-bar-amber";
        return "progress-bar-red";
    }
}