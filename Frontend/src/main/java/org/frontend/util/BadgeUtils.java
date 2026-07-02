package org.frontend.util;

import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import org.frontend.models.Issue;

public class BadgeUtils {

    public static void applyBadgeStyle(Label label, String value) {
        if (!label.getStyleClass().contains("badge")) {
            label.getStyleClass().add("badge");
        }

        if (value == null || value.isBlank()) {
            label.setText("UNKNOWN");
            label.getStyleClass().removeIf(c -> c.startsWith("badge-"));
            label.getStyleClass().add("badge-unknown");
            return;
        }

        label.setText(value);

        label.getStyleClass().removeIf(c -> c.startsWith("badge-"));

        String cssClass = switch (value) {
            case "TODO" -> "badge-todo";
            case "IN_PROGRESS" -> "badge-in-progress";
            case "DONE" -> "badge-resolved";
            default -> "badge-" + value.toLowerCase().replace("_", "-");
        };

        label.getStyleClass().add(cssClass);
    }

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

                applyBadgeStyle(badge, val);

                setGraphic(badge);
                setText(null);
            }
        };
    }
}