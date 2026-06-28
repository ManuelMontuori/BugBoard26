package org.frontend.util;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import org.frontend.models.UserWorkload;

public class WorkloadUI {

    public static HBox createWorkloadRow(UserWorkload u, long max, boolean consigliato) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("user-workload-row");
        if (consigliato) row.getStyleClass().add("user-workload-row-selected");

        Label avatar = new Label(getInitials(u.getFullName()));
        avatar.getStyleClass().addAll("avatar", "avatar-blue");

        Label nome = new Label(u.getFullName());
        nome.getStyleClass().add("fval");
        HBox.setHgrow(nome, Priority.ALWAYS);

        ProgressBar bar = new ProgressBar((double) u.getIssuesCount() / max);
        bar.setPrefWidth(80);
        bar.setPrefHeight(5);
        bar.getStyleClass().add(getBarColorClass(u.getIssuesCount(), max));

        Label count = new Label(String.valueOf(u.getIssuesCount()));
        count.getStyleClass().add("label-muted");
        count.setMinWidth(24);

        row.getChildren().addAll(avatar, nome, bar, count);
        if (consigliato) {
            Label chip = new Label("consigliato");
            chip.getStyleClass().add("ai-chip");
            row.getChildren().add(chip);
        }
        return row;
    }

    private static String getInitials(String fullName) {
        String[] parts = fullName.trim().split(" ");
        if (parts.length >= 2) return String.valueOf(parts[0].charAt(0)) + parts[1].charAt(0);
        return fullName.substring(0, Math.min(2, fullName.length())).toUpperCase();
    }

    private static String getBarColorClass(long count, long max) {
        double ratio = (double) count / max;
        if (ratio < 0.35) return "progress-bar-green";
        if (ratio < 0.70) return "progress-bar-amber";
        return "progress-bar-red";
    }
}