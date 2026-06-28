package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.frontend.controllers.IssueController;
import org.frontend.util.DialogUtils;
import org.frontend.util.FormUtil;

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

    @FXML private ToggleButton btnBug, btnFeature, btnDocumentation, btnQuestion;
    @FXML private ToggleButton btnHigh, btnMedium, btnLow;

    private IssueController issueController;

    public void initDependencies(IssueController issueController) {
        this.issueController = issueController;
    }

    @FXML
    private void initialize() {
        // Assegnazione dati. (Vedi Tip sotto per rimuovere anche questo!)
        btnBug.setUserData("BUG");
        btnFeature.setUserData("FEATURE");
        btnDocumentation.setUserData("DOCUMENTATION");
        btnQuestion.setUserData("QUESTION");
        btnHigh.setUserData("HIGH");
        btnMedium.setUserData("MEDIUM");
        btnLow.setUserData("LOW");
    }

    @FXML
    private void onCreaClicked() {
        if (!isFormValid()) return;

        // Estrazione dati delegata alla utility
        String title = fieldTitle.getText().trim();
        String description = fieldDescription.getText().trim();
        String type = FormUtil.getToggleUserData(typeGroup);
        String priority = FormUtil.getToggleUserData(priorityGroup);

        btnCreaIssue.setDisable(true);

        try {
            issueController.createIssue(title, description, type, priority);

            DialogUtils.mostraInformazione("Operazione Completata", "L'Issue è stata creata con successo!");
            svuotaForm();
        } catch (Exception e) {
            DialogUtils.mostraErrore("Errore di Rete",
                    "Il server non ha risposto correttamente. Riprova più tardi.",
                    "Impossibile salvare l'issue");
            e.printStackTrace();
        } finally {
            btnCreaIssue.setDisable(false);
        }
    }

    @FXML
    private void onAnnullaClicked() {
        // MainController.getInstance().loadView("home-dashboard.fxml");
    }

    private boolean isFormValid() {
        // La validazione ora è coesa e pulita
        boolean isTitleValid = FormUtil.checkNotBlank(fieldTitle, errTitle, "Il titolo è obbligatorio.");
        boolean isDescValid = FormUtil.checkNotBlank(fieldDescription, errDescription, "La descrizione è obbligatoria.");

        return isTitleValid && isDescValid;
    }

    private void svuotaForm() {
        fieldTitle.clear();
        fieldDescription.clear();

        FormUtil.clearToggleGroup(typeGroup);
        FormUtil.clearToggleGroup(priorityGroup);
    }
}