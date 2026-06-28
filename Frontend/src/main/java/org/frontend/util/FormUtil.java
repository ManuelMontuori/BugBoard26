package org.frontend.util;

import javafx.scene.control.Label;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;

public class FormUtil {

    // 1. Logica visiva degli errori
    public static void showError(Label lbl, String msg) {
        lbl.setText(msg);
        lbl.setVisible(true);
        lbl.setManaged(true);
    }

    public static void hideError(Label lbl) {
        lbl.setVisible(false);
        lbl.setManaged(false);
    }

    // 2. Logica di validazione standard (funziona sia per TextField che per TextArea)
    public static boolean checkNotBlank(TextInputControl inputField, Label errorLabel, String errorMessage) {
        if (inputField.getText() == null || inputField.getText().trim().isEmpty()) {
            showError(errorLabel, errorMessage);
            return false;
        } else {
            hideError(errorLabel);
            return true;
        }
    }

    // 3. Estrazione sicura dai ToggleGroup
    public static String getToggleUserData(ToggleGroup group) {
        return (group.getSelectedToggle() != null)
                ? (String) group.getSelectedToggle().getUserData()
                : null;
    }

    // 4. Reset dei ToggleGroup
    public static void clearToggleGroup(ToggleGroup group) {
        if (group.getSelectedToggle() != null) {
            group.getSelectedToggle().setSelected(false);
        }
    }

    // 1. Validazione Email
    public static boolean checkEmail(TextInputControl inputField, Label errorLabel) {
        String email = inputField.getText();
        if (email == null || email.trim().isEmpty()) {
            showError(errorLabel, "L'email è obbligatoria.");
            return false;
        } else if (!email.matches("^[\\w._%+\\-]+@[\\w.\\-]+\\.[a-zA-Z]{2,}$")) {
            showError(errorLabel, "Inserisci un indirizzo email valido.");
            return false;
        } else {
            hideError(errorLabel);
            return true;
        }
    }

    // 2. Validazione ToggleGroup
    public static boolean checkToggleSelected(ToggleGroup group, Label errorLabel, String errorMessage) {
        if (group.getSelectedToggle() == null) {
            showError(errorLabel, errorMessage);
            return false;
        } else {
            hideError(errorLabel);
            return true;
        }
    }

    // 3. Mostra Esito con Classi CSS dinamiche
    public static void showFeedback(Label lbl, String msg, boolean success) {
        lbl.setText(msg);
        lbl.getStyleClass().removeAll("label-success", "label-danger");
        lbl.getStyleClass().add(success ? "label-success" : "label-danger");
        lbl.setVisible(true);
        lbl.setManaged(true);
    }

    public static void hideFeedback(Label lbl) {
        lbl.setVisible(false);
        lbl.setManaged(false);
    }
}