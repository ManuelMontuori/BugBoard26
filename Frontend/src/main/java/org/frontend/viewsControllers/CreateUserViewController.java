package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.frontend.controllers.UserController;
import org.frontend.models.User; // Sostituisci con il tuo modello Utente effettivo se diverso

public class CreateUserViewController {

    // Componenti Tabella (Colonna Sinistra)
    @FXML private TableView<User> tblUsers;
    @FXML private TableColumn<User, String> colNome;
    @FXML private TableColumn<User, String> colCognome;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colRuolo;
    @FXML private TableColumn<User, Boolean> colStato;

    // Componenti Form (Colonna Destra)
    @FXML private TextField    fieldEmail;
    @FXML private TextField    fieldNome;
    @FXML private TextField    fieldCognome;
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
        // Form Inizializzazione
        btnUser.setUserData("USER");
        btnAdmin.setUserData("ADMIN");

        // Configurazione Colonne Tabella
        // Nota: Assicurati che i nomi corrispondano alle proprietà (es. getFullName(), getEmail(), getRole())
        colNome.setCellValueFactory(data -> data.getValue().firstNameProperty());
        colCognome.setCellValueFactory(data -> data.getValue().lastNameProperty());
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRuolo.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Mappatura booleana dello stato attivo
        colStato.setCellValueFactory(data -> data.getValue().activeProperty());
        colStato.setCellFactory(col -> creaSwitchStatoCell());

        // Caricamento dati e binding della TableView
        controller.loadAllUsers(); // Carica gli utenti dal server
        tblUsers.setItems(controller.getUsers());
    }

    /**
     * Genera una cella contenente una CheckBox interattiva che agisce da interruttore
     * per abilitare o disabilitare l'utente chiamando le API /enable e /disable.
     */
    private TableCell<User, Boolean> creaSwitchStatoCell() {
        return new TableCell<>() {
            private final CheckBox cb = new CheckBox();

            {
                cb.setOnAction(e -> {
                    User user = getTableRow().getItem();
                    if (user != null) {
                        boolean selezionato = cb.isSelected();
                        try {
                            if (selezionato) {
                                controller.enableUser(user.getUuid());
                            } else {
                                controller.disableUser(user.getUuid());
                            }
                        } catch (Exception ex) {
                            // Se la chiamata API fallisce, rimettiamo lo switch allo stato precedente
                            cb.setSelected(!selezionato);
                            System.err.println("Impossibile aggiornare lo stato dell'utente.");
                            ex.printStackTrace();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    cb.setSelected(item);
                    setGraphic(cb);
                }
            }
        };
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
            controller.loadWorkload(); // Ricarica la tabella a sinistra per mostrare il nuovo inserito
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

        String firstName = fieldNome.getText().trim();
        String lastName =  fieldCognome.getText().trim();
        if(firstName.isEmpty()) {
            showError(errNome, "Inserisci un nome valido.");
            ok = false;
        } else hideError(errNome);

        if(lastName.isEmpty()) {
            showError(errCognome, "Inserisci un cognome valido.");
            ok = false;
        } else hideError(errCognome);

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
        fieldNome.clear();
        fieldCognome.clear();
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