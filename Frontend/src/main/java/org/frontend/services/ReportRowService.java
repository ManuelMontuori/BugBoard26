package org.frontend.services;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.frontend.models.dtos.UserReportDTO;

public class ReportRowService {

    public static HBox creaRigaUtente(UserReportDTO r, int maxWorkload) {
        HBox row = new HBox();
        row.getStyleClass().add("report-row");
        row.setAlignment(Pos.CENTER_LEFT);

        Label avatar = new Label(calcolaIniziali(r.firstName(), r.lastName()));
        avatar.getStyleClass().addAll("avatar-label", determinaColoreAvatar(r.firstName()));

        Label nome = new Label(r.firstName() + " " + r.lastName());
        nome.getStyleClass().add("report-td");
        nome.setPrefWidth(130);

        HBox memberBox = new HBox(8, avatar, nome);
        memberBox.setAlignment(Pos.CENTER_LEFT);
        memberBox.setPrefWidth(170);

        Label tot = creaCellaNumerica(r.totIssue(), 60);
        Label create = creaCellaNumerica(r.totCreatedIssue(), 60);
        Label risolte = creaCellaNumerica(r.totResolvedIssue(), 65);

        int wl = r.totWorkloadIssue() != null ? r.totWorkloadIssue() : 0;
        ProgressBar pb = new ProgressBar(maxWorkload > 0 ? (double) wl / maxWorkload : 0);
        pb.getStyleClass().add("report-progress");
        if ((double) wl / maxWorkload > 0.75) pb.getStyleClass().add("high");
        else if ((double) wl / maxWorkload > 0.4) pb.getStyleClass().add("medium");
        pb.setPrefWidth(64);

        Label wlNum = new Label(String.valueOf(wl));
        wlNum.getStyleClass().add("report-td-muted");
        wlNum.setStyle("-fx-font-size:11px;");

        VBox wlBox = new VBox(2, wlNum, pb);
        wlBox.setPrefWidth(80);
        wlBox.setAlignment(Pos.CENTER_LEFT);

        String tempoStr = r.averageResolutionTime() != null ? String.format("%.1f gg", r.averageResolutionTime()) : "—";
        Label avg = new Label(tempoStr);
        avg.getStyleClass().add("report-td");
        avg.setPrefWidth(100);

        int pri = r.totHighPriorityIssue() != null ? r.totHighPriorityIssue() : 0;
        Label priBadge = new Label(String.valueOf(pri));
        priBadge.getStyleClass().addAll("badge", pri > 3 ? "badge-danger" : "badge-warning");
        HBox priBox = new HBox(priBadge);
        priBox.setPrefWidth(80);
        priBox.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(memberBox, tot, create, risolte, wlBox, avg, priBox);
        return row;
    }

    public static HBox creaRigaTop(UserReportDTO r, String medaglia, String rankStyleClass) {
        HBox row = new HBox(10);
        row.getStyleClass().add("top-performer-row");
        row.setAlignment(Pos.CENTER_LEFT);

        Label rank = new Label(medaglia);
        rank.getStyleClass().addAll("top-performer-rank", rankStyleClass);

        Label avatar = new Label(calcolaIniziali(r.firstName(), r.lastName()));
        avatar.getStyleClass().addAll("avatar-label", determinaColoreAvatar(r.firstName()));

        Label nome = new Label(r.firstName() + " " + r.lastName());
        nome.getStyleClass().add("top-performer-name");
        HBox.setHgrow(nome, Priority.ALWAYS);

        Label score = new Label((r.totResolvedIssue() != null ? r.totResolvedIssue() : 0) + " risolte");
        score.getStyleClass().add("top-performer-score");

        row.getChildren().addAll(rank, avatar, nome, score);
        return row;
    }

    // UTILITY INTERNE ALLA GRAFICA
    public static String calcolaIniziali(String nome, String cognome) {
        String n = (nome != null && !nome.isEmpty()) ? nome.substring(0, 1).toUpperCase() : "";
        String c = (cognome != null && !cognome.isEmpty()) ? cognome.substring(0, 1).toUpperCase() : "";
        return n + c;
    }

    private static String determinaColoreAvatar(String nome) {
        String[] colori = {"avatar-blue", "avatar-green", "avatar-orange", "avatar-red", "avatar-purple", "avatar-teal"};
        if (nome == null || nome.isEmpty()) return colori[0];
        return colori[Math.abs(nome.hashCode()) % colori.length];
    }

    private static Label creaCellaNumerica(Integer val, double prefWidth) {
        Label l = new Label(val != null ? String.valueOf(val) : "—");
        l.getStyleClass().add("report-td");
        l.setPrefWidth(prefWidth);
        return l;
    }
}