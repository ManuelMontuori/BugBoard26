package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
        import org.frontend.controllers.IssueController;

public class CreateIssueViewController {

    @FXML private TextField fieldTitle;
    @FXML private TextArea fieldDescription;
    @FXML private ToggleGroup typeGroup;
    @FXML private ToggleGroup priorityGroup;
    @FXML private Label errTitle;
    @FXML private Label errDescription;
    @FXML private Label errType;
    @FXML private Label errPriority;
    @FXML private Button btnCreaIssue;

    private final IssueController controller = new IssueController();

    @FXML
    private void onCreaClicked() {
        if (!validate()) return;

        String title       = fieldTitle.getText().trim();
        String description = fieldDescription.getText().trim();
        // Se c'è un toggle selezionato prendi i suoi dati, altrimenti imposta la stringa a null
        String type = (typeGroup.getSelectedToggle() != null)
                ? (String) typeGroup.getSelectedToggle().getUserData()
                : null;

        String priority = (priorityGroup.getSelectedToggle() != null)
                ? (String) priorityGroup.getSelectedToggle().getUserData()
                : null;

        // 1. DISABILITA i controlli per evitare click compulsivi dell'utente durante la chiamata di rete
        // (Presumo tu abbia un'annotazione @FXML per il bottone, es: btnCrea)
        btnCreaIssue.setDisable(true);

        try {
            // 2. Invia i dati al backend
            controller.createIssue(title, description, type, priority);

            // 3. Mostra un messaggio di successo
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Operazione Completata");
            alert.setHeaderText(null);
            alert.setContentText("L'Issue è stata creata con successo!");
            alert.showAndWait(); // Resta aperto finché l'utente non clicca "OK"

            svuotaForm();

        } catch (Exception e) {
            // 5. Se il server è giù o c'è un errore, avvisa l'utente senza far crashare l'app
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore di Rete");
            alert.setHeaderText("Impossibile salvare l'Issue");
            alert.setContentText("Il server non ha risposto correttamente. Riprova più tardi.");
            alert.showAndWait();

            e.printStackTrace();
        } finally {
            // Ripristina il bottone in caso di errore per permettere di riprovare
            // btnCrea.setDisable(false);
        }
    }

    @FXML
    private void onAnnullaClicked() {
        // MainController.getInstance().loadView("home-dashboard.fxml");
    }

    private boolean validate() {
        boolean ok = true;

        if (fieldTitle.getText().isBlank()) {
            showError(errTitle, "Il titolo è obbligatorio.");
            ok = false;
        } else {
            hideError(errTitle);
        }

        if (fieldDescription.getText().isBlank()) {
            showError(errDescription, "La descrizione è obbligatoria.");
            ok = false;
        } else {
            hideError(errDescription);
        }

        return ok;
    }

    private void showError(Label lbl, String msg) {
        lbl.setText(msg);
        lbl.setVisible(true);
        lbl.setManaged(true);
    }

    private void hideError(Label lbl) {
        lbl.setVisible(false);
        lbl.setManaged(false);
    }

    private void svuotaForm() {
        fieldTitle.clear();
        fieldDescription.clear();

        if (typeGroup.getSelectedToggle() != null) {
            typeGroup.getSelectedToggle().setSelected(false);
        }
        if (priorityGroup.getSelectedToggle() != null) {
            priorityGroup.getSelectedToggle().setSelected(false);
        }

        // Il bottone torna attivo e pronto all'uso!
        btnCreaIssue.setDisable(false);
    }
}