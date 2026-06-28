package org.frontend.viewsControllers;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import org.frontend.models.Notification;
import org.frontend.services.NotificationService;
import org.frontend.util.NotificationUI;

import java.util.List;

public class NotificationViewController {

    @FXML private Label  lblTotale;
    @FXML private Label  lblNonLette;
    @FXML private Button btnSegnatutte;
    @FXML private VBox   listNotifiche;

    private NotificationService notificationService;

    public void initDependencies(NotificationService notificationService) {
        this.notificationService = notificationService;
        aggiornaUI();

        this.notificationService.getNotifications().addListener(
                (ListChangeListener<Notification>) c -> Platform.runLater(this::aggiornaUI)
        );
    }

    @FXML
    public void initialize() {
        // Nessuna logica qui, aspettiamo l'iniezione delle dipendenze
    }

    private void aggiornaUI() {
        if (notificationService == null) return;

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
                .forEach(n -> listNotifiche.getChildren().add(
                        // Deleghiamo a NotificationUI e passiamo il metodo da eseguire al click!
                        NotificationUI.createNotificationRow(n, () -> onToggleLetta(n))
                ));
    }

    private void aggiornaContatore(List<Notification> list) {
        long nonLette = list.stream().filter(n -> !n.isRead()).count();
        lblNonLette.setText(nonLette > 0 ? nonLette + " non lette" : "Tutte lette");

        lblNonLette.getStyleClass().removeAll("badge-unread", "badge-read");
        lblNonLette.getStyleClass().add(nonLette > 0 ? "badge-unread" : "badge-read");
    }

    private void onToggleLetta(Notification n) {
        if (notificationService == null) return;

        // 1. Diciamo al servizio di aggiornare il dato
        notificationService.setRead(n.getId(), !n.isRead());

        // 2. Ridisegniamo la UI.
        // MOLTO più sicuro che cercare i componenti a mano con row.getChildren().get(0)!
        aggiornaUI();
    }

    @FXML
    private void onSegnatutte() {
        if (notificationService == null) return;

        notificationService.getNotifications().stream()
                .filter(n -> !n.isRead())
                .toList()
                .forEach(n -> notificationService.setRead(n.getId(), true));

        Platform.runLater(this::aggiornaUI);
    }
}