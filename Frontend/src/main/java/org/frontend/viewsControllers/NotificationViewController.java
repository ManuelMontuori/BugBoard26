package org.frontend.viewsControllers;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.frontend.models.Notification;
import org.frontend.services.AuthSession;
import org.frontend.services.NotificationService;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NotificationViewController {

    @FXML private Label  lblTotale;
    @FXML private Label  lblNonLette;
    @FXML private Button btnSegnatutte;
    @FXML private VBox   listNotifiche;

    private final NotificationService notificationService =
            AuthSession.getInstance().getNotificationService();

    private static final DateTimeFormatter FMT = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    // ════════════════════════════════════════════════════════════════════════
    // INITIALIZE
    // ════════════════════════════════════════════════════════════════════════
    @FXML
    public void initialize() {
        // loadNotifications() già chiamato dal DashboardController all'avvio
        aggiornaUI();

        // Scatta sia quando si aggiunge/rimuove un elemento
        // sia quando cambia readProperty() grazie all'extractor nella lista
        notificationService.getNotifications().addListener(
                (ListChangeListener<Notification>) c -> Platform.runLater(this::aggiornaUI)
        );
    }

    // ════════════════════════════════════════════════════════════════════════
    // AGGIORNA UI COMPLETA
    // ════════════════════════════════════════════════════════════════════════
    private void aggiornaUI() {
        List<Notification> list = notificationService.getNotifications();

        lblTotale.setText(list.size() + " notifiche");
        aggiornaContatore(list);

        listNotifiche.getChildren().clear();

        if (list.isEmpty()) {
            Label vuoto = new Label("Non hai ancora ricevuto notifiche.");
            vuoto.getStyleClass().add("label-muted");
            listNotifiche.getChildren().add(vuoto);
            return;
        }

        list.stream()
                .sorted((a, b) -> Boolean.compare(a.isRead(), b.isRead()))
                .forEach(n -> listNotifiche.getChildren().add(creaRiga(n)));
    }

    // ════════════════════════════════════════════════════════════════════════
    // AGGIORNA SOLO IL CONTATORE (senza ricostruire tutta la lista)
    // ════════════════════════════════════════════════════════════════════════
    private void aggiornaContatore(List<Notification> list) {
        long nonLette = list.stream().filter(n -> !n.isRead()).count();
        lblNonLette.setText(nonLette > 0 ? nonLette + " non lette" : "Tutte lette");
        lblNonLette.getStyleClass().removeAll("badge-unread", "badge-read");
        lblNonLette.getStyleClass().add(nonLette > 0 ? "badge-unread" : "badge-read");
    }

    // ════════════════════════════════════════════════════════════════════════
    // CREA RIGA SINGOLA
    // ════════════════════════════════════════════════════════════════════════
    private HBox creaRiga(Notification n) {
        HBox row = new HBox(12);
        row.getStyleClass().addAll("notif-row", n.isRead() ? "notif-read" : "notif-unread");
        row.setAlignment(Pos.CENTER_LEFT);

        // Pallino letto/non letto
        Label dot = new Label();
        dot.getStyleClass().add(n.isRead() ? "notif-dot-read" : "notif-dot-unread");

        // Icona tipo
        Label icon = new Label(iconaPerTipo(n.getType()));
        icon.getStyleClass().add("notif-icon");

        // Corpo: messaggio + data
        VBox body = new VBox(3);
        HBox.setHgrow(body, Priority.ALWAYS);

        Label msg = new Label(n.getMessage() != null ? n.getMessage() : "");
        msg.getStyleClass().add(n.isRead() ? "notif-message-read" : "notif-message");
        msg.setWrapText(true);

        Label data = new Label(formattaData(n.getCreatedAt()));
        data.getStyleClass().add("notif-date");

        body.getChildren().addAll(msg, data);

        // Pulsante toggle
        Button btn = new Button(n.isRead() ? "Non letta" : "Segna letta");
        btn.getStyleClass().add(n.isRead() ? "btn-ghost" : "btn-primary-sm");
        btn.setOnAction(e -> onToggleLetta(n, row, btn));

        row.getChildren().addAll(dot, icon, body, btn);
        return row;
    }

    // ════════════════════════════════════════════════════════════════════════
    // TOGGLE LETTA / NON LETTA
    // ════════════════════════════════════════════════════════════════════════
    private void onToggleLetta(Notification n, HBox row, Button btn) {
        boolean nuovoStato = !n.isRead();

        // 1. Aggiorna backend + oggetto nella lista
        notificationService.setRead(n.getId(), nuovoStato);

        // 2. Aggiorna stile della riga
        row.getStyleClass().removeAll("notif-read", "notif-unread");
        row.getStyleClass().add(nuovoStato ? "notif-read" : "notif-unread");

        // 3. Aggiorna pallino
        Label dot = (Label) row.getChildren().get(0);
        dot.getStyleClass().removeAll("notif-dot-read", "notif-dot-unread");
        dot.getStyleClass().add(nuovoStato ? "notif-dot-read" : "notif-dot-unread");

        // 4. Aggiorna testo messaggio
        VBox body = (VBox) row.getChildren().get(2);
        Label msg = (Label) body.getChildren().get(0);
        msg.getStyleClass().removeAll("notif-message", "notif-message-read");
        msg.getStyleClass().add(nuovoStato ? "notif-message-read" : "notif-message");

        // 5. Aggiorna pulsante
        btn.setText(nuovoStato ? "Non letta" : "Segna letta");
        btn.getStyleClass().removeAll("btn-primary-sm", "btn-ghost");
        btn.getStyleClass().add(nuovoStato ? "btn-ghost" : "btn-primary-sm");

        // 6. Aggiorna contatore DOPO che setRead ha modificato l'oggetto
        aggiornaContatore(notificationService.getNotifications());
    }

    // ════════════════════════════════════════════════════════════════════════
    // SEGNA TUTTE LETTE
    // ════════════════════════════════════════════════════════════════════════
    @FXML
    private void onSegnatutte() {
        notificationService.getNotifications().stream()
                .filter(n -> !n.isRead())
                .toList() // evita ConcurrentModificationException
                .forEach(n -> notificationService.setRead(n.getId(), true));

        Platform.runLater(this::aggiornaUI);
    }

    // ════════════════════════════════════════════════════════════════════════
    // UTILITY
    // ════════════════════════════════════════════════════════════════════════
    private String iconaPerTipo(String tipo) {
        if (tipo == null) return "🔔";
        return switch (tipo.toUpperCase()) {
            case "ISSUE_ASSIGNED" -> "📋";
            case "ISSUE_RESOLVED" -> "✅";
            case "ISSUE_CREATED"  -> "🐛";
            case "ISSUE_UPDATED"  -> "✏️";
            case "COMMENT_ADDED"  -> "💬";
            default               -> "🔔";
        };
    }

    private String formattaData(String isoDate) {
        if (isoDate == null || isoDate.isBlank()) return "";
        try {
            return FMT.format(Instant.parse(isoDate));
        } catch (Exception e) {
            return isoDate;
        }
    }
}