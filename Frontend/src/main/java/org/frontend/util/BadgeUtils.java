package org.frontend.util;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import org.frontend.models.Issue;

public class BadgeUtils {

    public static void applyBadgeStyle(Label label, String value) {
        // --- IL PEZZO MANCANTE: Assicuriamoci che ci sia SEMPRE la forma base ---
        if (!label.getStyleClass().contains("badge")) {
            label.getStyleClass().add("badge");
        }
        // ------------------------------------------------------------------------

        if (value == null || value.isBlank()) {
            label.setText("UNKNOWN");
            label.getStyleClass().removeIf(c -> c.startsWith("badge-"));
            label.getStyleClass().add("badge-unknown");
            return;
        }

        // 1. Assegna il testo
        label.setText(value);

        // 2. Rimuove solo i colori vecchi, salva la formattazione ("badge")
        label.getStyleClass().removeIf(c -> c.startsWith("badge-"));

        // 3. La tua mappatura esatta per gli stati e i tipi
        String cssClass = switch (value) {
            case "TODO"        -> "badge-todo";
            case "IN_PROGRESS" -> "badge-in-progress";
            case "DONE"        -> "badge-resolved";
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

                // Ora quando chiamiamo applyBadgeStyle, il metodo capirà che manca
                // la classe "badge" e la aggiungerà in automatico, oltre ai colori!
                applyBadgeStyle(badge, val);

                setGraphic(badge);
                setText(null);
            }
        };
    }
}