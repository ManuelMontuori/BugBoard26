package org.frontend.util;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import org.frontend.models.Issue;

public class BadgeUtils {

    public static void applyBadgeStyle(Label label, String value) {
        if (value == null || value.isBlank()) {
            label.setText("UNKNOWN");
            label.getStyleClass().removeIf(c -> c.startsWith("badge-"));
            label.getStyleClass().add("badge-unknown");
            return;
        }

        // 1. RIPRISTINATO: Assegna il testo!
        label.setText(value);

        // 2. RIPRISTINATO: Rimuove solo i colori vecchi, salva la formattazione FXML
        label.getStyleClass().removeIf(c -> c.startsWith("badge-"));

        // 3. RIPRISTINATA: La tua mappatura esatta per gli stati e i tipi
        String cssClass = switch (value) {
            case "TODO"        -> "badge-todo";
            case "IN_PROGRESS" -> "badge-in-progress";
            case "DONE"        -> "badge-resolved"; // Mantiene il tuo CSS
            default            -> "badge-" + value.toLowerCase().replace("_", "-");
        };

        label.getStyleClass().add(cssClass);
    }

    // Crea una TableCell stilizzata come badge (usato nella TableView)
    public static TableCell<Issue, String> createBadgeCell() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label badge = new Label(val);
                applyBadgeStyle(badge, val); // Riutilizziamo il metodo sopra!
                setGraphic(badge);
                setText(null);
            }
        };
    }
}