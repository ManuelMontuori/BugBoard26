package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
        import org.frontend.controllers.IssueController;

public class CreateIssueViewController {

    @FXML private TextField    fieldTitle;
    @FXML private TextArea     fieldDescription;
    @FXML private ToggleGroup  typeGroup;
    @FXML private ToggleGroup  priorityGroup;
    @FXML private Label        errTitle;
    @FXML private Label        errDescription;
    @FXML private Label        errType;
    @FXML private Label        errPriority;

    private final IssueController controller = new IssueController();

    @FXML
    public void initialize() {
        // niente da inizializzare, i ToggleGroup vengono dall'FXML
    }

    @FXML
    private void onCreaClicked() {
        if (!validate()) return;

        String title       = fieldTitle.getText().trim();
        String description = fieldDescription.getText().trim();
        String type        = (String) typeGroup.getSelectedToggle().getUserData();
        String priority    = (String) priorityGroup.getSelectedToggle().getUserData();

        controller.createIssue(title, description, type, priority);

        // Torna alla dashboard
        // MainController.getInstance().loadView("home-dashboard.fxml");
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

        if (typeGroup.getSelectedToggle() == null) {
            showError(errType, "Seleziona un tipo.");
            ok = false;
        } else {
            hideError(errType);
        }

        if (priorityGroup.getSelectedToggle() == null) {
            showError(errPriority, "Seleziona una priorità.");
            ok = false;
        } else {
            hideError(errPriority);
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
}