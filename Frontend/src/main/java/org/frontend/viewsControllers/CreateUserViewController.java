package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.frontend.controllers.UserController;

public class CreateUserViewController {

    @FXML private TextField    fieldEmail;
    @FXML private TextField    fieldNome;
    @FXML private TextField fieldCognome;
    @FXML private ToggleButton btnUser;
    @FXML private ToggleButton btnAdmin;
    @FXML private ToggleGroup  roleGroup;
    @FXML private Label        errEmail;
    @FXML private Label        errRole;
    @FXML private Label        errNome;
    @FXML private Label        errCognome;
    @FXML private Label        lblEsito;

    private final UserController controller = new UserController();

    @FXML
    public void initialize() {
        btnUser.setUserData("USER");
        btnAdmin.setUserData("ADMIN");
    }

    @FXML
    private void onCreaClicked() {
        if (!validate()) return;

        String email = fieldEmail.getText().trim();
        String role  = (String) roleGroup.getSelectedToggle().getUserData();
        String firstName = fieldNome.getText().trim();
        String lastName = fieldCognome.getText().trim();

        try {
            controller.createUser(email, role, firstName, lastName);
            mostraEsito("Utente " + email + " creato con successo.", true);
            resetForm();
        } catch (Exception e) {
            mostraEsito("Errore durante la creazione. Riprova.", false);
        }
    }

    @FXML
    private void onAnnullaClicked() {
        // MainController.getInstance().loadView("home-dashboard.fxml");
    }

    private boolean validate() {
        boolean ok = true;

        // Validazione email
        String email = fieldEmail.getText().trim();
        if (email.isBlank()) {
            showError(errEmail, "L'email è obbligatoria.");
            ok = false;
        } else if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            showError(errEmail, "Inserisci un indirizzo email valido.");
            ok = false;
        } else {
            hideError(errEmail);
        }

        // Validazione nome e cognome
        String firstName = fieldNome.getText().trim();
        String lastName =  fieldCognome.getText().trim();
        if(firstName.isEmpty()) {
            showError(errNome, "Inserisci un nome valido.");
            ok = false;
        }
        else hideError(errNome);
        if(lastName.isEmpty()) {
            showError(errCognome, "Inserisci un cognome valido.");
            ok = false;
        }
        else hideError(errCognome);

        // Validazione ruolo
        if (roleGroup.getSelectedToggle() == null) {
            showError(errRole, "Seleziona un ruolo.");
            ok = false;
        } else {
            hideError(errRole);
        }

        return ok;
    }

    private void resetForm() {
        fieldEmail.clear();
        roleGroup.selectToggle(null);
    }

    private void mostraEsito(String msg, boolean successo) {
        lblEsito.setText(msg);
        lblEsito.getStyleClass().removeAll("label-success", "label-danger");
        lblEsito.getStyleClass().add(successo ? "label-success" : "label-danger");
        lblEsito.setVisible(true);
        lblEsito.setManaged(true);
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
