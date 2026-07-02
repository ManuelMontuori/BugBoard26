package org.frontend.util;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.frontend.models.Notification;

public class NotificationUI {

    public static HBox createNotificationRow(Notification n, Runnable onToggleAction) {
        HBox row = new HBox(12);
        row.getStyleClass().addAll("notif-row", n.isRead() ? "notif-read" : "notif-unread");
        row.setAlignment(Pos.CENTER_LEFT);

        Label dot = new Label();
        dot.getStyleClass().add(n.isRead() ? "notif-dot-read" : "notif-dot-unread");

        Label icon = new Label("🔔");
        icon.getStyleClass().add("notif-icon");

        VBox body = new VBox(3);
        HBox.setHgrow(body, Priority.ALWAYS);

        Label msg = new Label(n.getMessage() != null ? n.getMessage() : "");
        msg.getStyleClass().add(n.isRead() ? "notif-message-read" : "notif-message");
        msg.setWrapText(true);

        Label data = new Label(DateUtils.formatIsoToLocal(n.getCreatedAt()));
        data.getStyleClass().add("notif-date");

        body.getChildren().addAll(msg, data);

        Button btn = new Button(n.isRead() ? "Non letta" : "Segna letta");
        btn.getStyleClass().add(n.isRead() ? "btn-ghost" : "btn-primary-sm");

        // Quando il pulsante viene cliccato, esegue l'azione passata dal Controller
        btn.setOnAction(e -> onToggleAction.run());

        row.getChildren().addAll(dot, icon, body, btn);
        return row;
    }

}