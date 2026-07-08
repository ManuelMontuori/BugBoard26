package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.frontend.controllers.UserController;
import org.frontend.models.User;
import org.frontend.util.DialogUtils;
import org.frontend.util.FormUtil;
import org.frontend.util.TableUtil;

public class CreateUserViewController {

    @FXML private TableView<User> tblUsers;
    @FXML private TableColumn<User, String> colNome;
    @FXML private TableColumn<User, String> colCognome;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRuolo;
    @FXML private TableColumn<User, Boolean> colStato;

    @FXML private TextField fieldEmail;
    @FXML private TextField fieldNome;
    @FXML private TextField fieldCognome;
    @FXML private ToggleGroup roleGroup;
    @FXML private ToggleButton btnUser, btnAdmin;


    @FXML private Label errEmail;
    @FXML private Label errRole;
    @FXML private Label errNome;
    @FXML private Label errCognome;
    @FXML private Label lblEsito;

    private UserController userController;

    public void initDependencies(UserController userController) {
        this.userController = userController;

        btnUser.setUserData("USER");
        btnAdmin.setUserData("ADMIN");


        tblUsers.setItems(userController.getUsers());
        caricaTuttiGliUtenti();
    }

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(data -> data.getValue().firstNameProperty());
        colCognome.setCellValueFactory(data -> data.getValue().lastNameProperty());
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRuolo.setCellValueFactory(new PropertyValueFactory<>("role"));
        colStato.setCellValueFactory(data -> data.getValue().activeProperty());

        colStato.setCellFactory(col -> TableUtil.createToggleSwitchCell((user, isSelected) -> {
            if (userController == null) return;
            if (isSelected) {
                userController.enableUser(user.getUuid());
            } else {
                userController.disableUser(user.getUuid());
            }
        }));
    }

    @FXML
    private void onCreaClicked() {
        if (userController == null) return;

        FormUtil.hideFeedback(lblEsito);
        if (!validateForm()) return;

        String email = fieldEmail.getText().trim();
        String role = FormUtil.getToggleUserData(roleGroup);
        String firstName = fieldNome.getText().trim();
        String lastName = fieldCognome.getText().trim();

        try {
            userController.createUser(email, role, firstName, lastName);
            String message = "Utente " + firstName + " " + lastName + " (" + email + ") creato con successo.";
            DialogUtils.showInfo("Successo", message);
            resetForm();
            userController.loadWorkload();
        } catch (Exception e) {
            e.printStackTrace();
            String message = "Errore durante la creazione dell'utente: " + firstName + " " + lastName + " (" + email + ").";
            DialogUtils.showError("Errore", message, "Utente non creato.");
        }
    }

    @FXML
    private void onAnnullaClicked() {
        resetForm();
    }

    private boolean validateForm() {
        boolean isEmailValid = FormUtil.checkEmail(fieldEmail, errEmail);
        boolean isNomeValid = FormUtil.checkNotBlank(fieldNome, errNome, "Inserisci un nome valido.");
        boolean isCognomeValid = FormUtil.checkNotBlank(fieldCognome, errCognome, "Inserisci un cognome valido.");
        boolean isRoleValid = FormUtil.checkToggleSelected(roleGroup, errRole, "Seleziona un ruolo.");

        return isEmailValid && isNomeValid && isCognomeValid && isRoleValid;
    }

    private void resetForm() {
        fieldEmail.clear();
        fieldNome.clear();
        fieldCognome.clear();
        FormUtil.clearToggleGroup(roleGroup);

        FormUtil.hideError(errEmail);
        FormUtil.hideError(errNome);
        FormUtil.hideError(errCognome);
        FormUtil.hideError(errRole);
    }

    private void caricaTuttiGliUtenti() {
        if (userController != null) {
            try {
                userController.loadAllUsers();
            } catch (Exception e) {
                DialogUtils.showError("Errore di rete.", "Errore durante il caricamento della lista degli utenti.", "Riprova");
                throw new RuntimeException("Errore durante il caricamento della lista de users.", e);
            }
        }
    }
}