package org.frontend.viewsControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.frontend.controllers.UserController;
import org.frontend.models.dtos.UserReportDTO;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ReportViewController {

    // ── Header ──────────────────────────────────────────────────────────────
    @FXML private ComboBox<String> comboMese;
    @FXML private Button           btnEsporta;

    // ── KPI ─────────────────────────────────────────────────────────────────
    @FXML private Label lblTotAperte;
    @FXML private Label lblTotAperteDesc;
    @FXML private Label lblTotRisolte;
    @FXML private Label lblTotRisolteDesc;
    @FXML private Label lblAvgTempo;
    @FXML private Label lblAltaPriorita;

    // ── Charts ───────────────────────────────────────────────────────────────
    @FXML private BarChart<String, Number> barChartWorkload;
    @FXML private PieChart                  pieChartStato;
    @FXML private PieChart                  pieChartPriorita;

    // ── Tabella + Top performer ──────────────────────────────────────────────
    @FXML private VBox listUtentiReport;
    @FXML private VBox topPerformerBox;

    // ────────────────────────────────────────────────────────────────────────
    // Uso dell'UserController integrato nell'architettura MVC
    private final UserController userController = new UserController();



    // ════════════════════════════════════════════════════════════════════════
    // INITIALIZE
    // ════════════════════════════════════════════════════════════════════════
    @FXML
    public void initialize() {
        List<String> mesiDinamici = generaUltimiMesi(12);

        comboMese.setItems(FXCollections.observableArrayList(mesiDinamici));
        comboMese.getSelectionModel().selectFirst();
        comboMese.setOnAction(e -> caricaReport());

        caricaReport();
    }

    // ════════════════════════════════════════════════════════════════════════
    // CARICA REPORT
    // ════════════════════════════════════════════════════════════════════════
    private void caricaReport() {
        String meseSelezionato = comboMese.getValue();
        if (meseSelezionato == null || meseSelezionato.isEmpty()) {
            mostraVuoto();
            return;
        }

        // Estrazione dinamica di anno e mese dai dati della ComboBox
        int anno = estraiAnno(meseSelezionato);
        int mese = estraiMese(meseSelezionato);

        // Chiamata al controller MVC passando i parametri richiesti dal backend
        List<UserReportDTO> report = userController.getMonthlyReport(anno, mese);

        if (report == null || report.isEmpty()) {
            mostraVuoto();
            return;
        }

        aggiornaKPI(report);
        aggiornaBarChart(report);
        aggiornaPieChartStato(report);
        aggiornaPieChartPriorita(report);
        aggiornaTabella(report);
        aggiornaTopPerformer(report);
    }

    // ════════════════════════════════════════════════════════════════════════
    // KPI
    // ════════════════════════════════════════════════════════════════════════
    private void aggiornaKPI(List<UserReportDTO> report) {
        int totAperte    = report.stream().mapToInt(r -> nullSafe(r.totIssue())).sum();
        int totRisolte   = report.stream().mapToInt(r -> nullSafe(r.totResolvedIssue())).sum();
        int altaPriorita = report.stream().mapToInt(r -> nullSafe(r.totHightPriorityIssue())).sum();
        double avgTempo  = report.stream()
                .filter(r -> r.averageResolutionTime() != null)
                .mapToDouble(UserReportDTO::averageResolutionTime)
                .average()
                .orElse(0.0);

        lblTotAperte.setText(String.valueOf(totAperte));
        lblTotRisolte.setText(String.valueOf(totRisolte));
        lblAltaPriorita.setText(String.valueOf(altaPriorita));
        lblAvgTempo.setText(String.format("%.1f gg", avgTempo));

        // Descrizioni dinamiche
        int pctRisolte = totAperte > 0 ? (totRisolte * 100 / totAperte) : 0;
        lblTotAperteDesc.setText("nel periodo selezionato");
        lblTotRisolteDesc.setText(pctRisolte + "% del totale");
    }

    // ════════════════════════════════════════════════════════════════════════
    // BAR CHART — workload per membro
    // ════════════════════════════════════════════════════════════════════════
    private void aggiornaBarChart(List<UserReportDTO> report) {
        barChartWorkload.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Workload attivo");

        report.stream()
                .filter(r -> r.totWorkloadIssue() != null)
                .sorted(Comparator.comparingInt(UserReportDTO::totWorkloadIssue).reversed())
                .limit(10)
                .forEach(r -> {
                    String etichetta = iniziali(r.firstName(), r.lastName());
                    series.getData().add(
                            new XYChart.Data<>(etichetta, r.totWorkloadIssue())
                    );
                });

        barChartWorkload.getData().add(series);

        // Tooltip su ogni barra
        series.getData().forEach(d -> {
            String nome = cercaNomeCompleto(report, d.getXValue());
            Tooltip.install(d.getNode(),
                    new Tooltip(nome + "\nWorkload: " + d.getYValue()));
        });
    }

    // ════════════════════════════════════════════════════════════════════════
    // PIE CHART — distribuzione stati
    // ════════════════════════════════════════════════════════════════════════
    private void aggiornaPieChartStato(List<UserReportDTO> report) {
        int totRisolte  = report.stream().mapToInt(r -> nullSafe(r.totResolvedIssue())).sum();
        int totWorkload = report.stream().mapToInt(r -> nullSafe(r.totWorkloadIssue())).sum();
        int totCreate   = report.stream().mapToInt(r -> nullSafe(r.totCreatedIssue())).sum();
        // "Aperte non in lavorazione" = create - risolte - workload (floor a 0)
        int altre = Math.max(0, totCreate - totRisolte - totWorkload);

        pieChartStato.getData().setAll(
                new PieChart.Data("Risolte ("      + totRisolte  + ")", totRisolte),
                new PieChart.Data("In lavoraz. ("  + totWorkload + ")", totWorkload),
                new PieChart.Data("Altre aperte ("  + altre       + ")", altre)
        );

        applicaTooltipPie(pieChartStato);
    }

    // ════════════════════════════════════════════════════════════════════════
    // PIE CHART — distribuzione priorità
    // ════════════════════════════════════════════════════════════════════════
    private void aggiornaPieChartPriorita(List<UserReportDTO> report) {
        int alta   = report.stream().mapToInt(r -> nullSafe(r.totHightPriorityIssue())).sum();
        int totale = report.stream().mapToInt(r -> nullSafe(r.totIssue())).sum();
        int altra  = Math.max(0, totale - alta);

        pieChartPriorita.getData().setAll(
                new PieChart.Data("Alta priorità (" + alta  + ")", alta),
                new PieChart.Data("Normale ("       + altra + ")", altra)
        );

        applicaTooltipPie(pieChartPriorita);
    }

    // ════════════════════════════════════════════════════════════════════════
    // TABELLA UTENTI
    // ════════════════════════════════════════════════════════════════════════
    private void aggiornaTabella(List<UserReportDTO> report) {
        listUtentiReport.getChildren().clear();

        int maxWorkload = report.stream()
                .mapToInt(r -> nullSafe(r.totWorkloadIssue()))
                .max().orElse(1);

        report.stream()
                .sorted(Comparator.comparingInt(
                        (UserReportDTO r) -> nullSafe(r.totWorkloadIssue())
                ).reversed())
                .forEach(r -> listUtentiReport.getChildren()
                        .add(creaRigaUtente(r, maxWorkload)));
    }

    private HBox creaRigaUtente(UserReportDTO r, int maxWorkload) {
        HBox row = new HBox();
        row.getStyleClass().add("report-row");
        row.setAlignment(Pos.CENTER_LEFT);

        // Avatar + Nome
        Label avatar = new Label(iniziali(r.firstName(), r.lastName()));
        avatar.getStyleClass().addAll("avatar-label", coloreAvatar(r.firstName()));

        Label nome = new Label(r.firstName() + " " + r.lastName());
        nome.getStyleClass().add("report-td");
        nome.setPrefWidth(130);

        HBox memberBox = new HBox(8, avatar, nome);
        memberBox.setAlignment(Pos.CENTER_LEFT);
        memberBox.setPrefWidth(170);

        // Colonne numeriche semplici
        Label tot     = tdNum(r.totIssue(),            60);
        Label create  = tdNum(r.totCreatedIssue(),     60);
        Label risolte = tdNum(r.totResolvedIssue(),    65);

        // Workload con progress bar
        int wl = nullSafe(r.totWorkloadIssue());
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

        // Tempo medio
        String tempoStr = r.averageResolutionTime() != null
                ? String.format("%.1f gg", r.averageResolutionTime()) : "—";
        Label avg = new Label(tempoStr);
        avg.getStyleClass().add("report-td");
        avg.setPrefWidth(100);

        // Alta priorità con badge
        int pri = nullSafe(r.totHightPriorityIssue());
        Label priBadge = new Label(String.valueOf(pri));
        priBadge.getStyleClass().addAll("badge", pri > 3 ? "badge-danger" : "badge-warning");
        HBox priBox = new HBox(priBadge);
        priBox.setPrefWidth(80);
        priBox.setAlignment(Pos.CENTER_LEFT);

        row.getChildren().addAll(memberBox, tot, create, risolte, wlBox, avg, priBox);
        return row;
    }

    // ════════════════════════════════════════════════════════════════════════
    // TOP PERFORMER
    // ════════════════════════════════════════════════════════════════════════
    private void aggiornaTopPerformer(List<UserReportDTO> report) {
        topPerformerBox.getChildren().clear();

        String[] medallie = {"🥇", "🥈", "🥉", "4.", "5."};
        String[] rankStyle = {
                "top-performer-rank-gold",
                "top-performer-rank-silver",
                "top-performer-rank-bronze",
                "top-performer-rank",
                "top-performer-rank"
        };

        List<UserReportDTO> top = report.stream()
                .filter(r -> r.totResolvedIssue() != null)
                .sorted(Comparator.comparingInt(UserReportDTO::totResolvedIssue).reversed())
                .limit(5)
                .toList();

        for (int i = 0; i < top.size(); i++) {
            topPerformerBox.getChildren().add(
                    creaRigaTop(top.get(i), medallie[i], rankStyle[i])
            );
        }
    }

    private HBox creaRigaTop(UserReportDTO r, String medaglia, String rankStyleClass) {
        HBox row = new HBox(10);
        row.getStyleClass().add("top-performer-row");
        row.setAlignment(Pos.CENTER_LEFT);

        Label rank = new Label(medaglia);
        rank.getStyleClass().addAll("top-performer-rank", rankStyleClass);

        Label avatar = new Label(iniziali(r.firstName(), r.lastName()));
        avatar.getStyleClass().addAll("avatar-label", coloreAvatar(r.firstName()));

        Label nome = new Label(r.firstName() + " " + r.lastName());
        nome.getStyleClass().add("top-performer-name");
        HBox.setHgrow(nome, Priority.ALWAYS);

        Label score = new Label(r.totResolvedIssue() + " risolte");
        score.getStyleClass().add("top-performer-score");

        row.getChildren().addAll(rank, avatar, nome, score);
        return row;
    }

    // ════════════════════════════════════════════════════════════════════════
    // STATO VUOTO
    // ════════════════════════════════════════════════════════════════════════
    private void mostraVuoto() {
        lblTotAperte.setText("0");
        lblTotRisolte.setText("0");
        lblAvgTempo.setText("— gg");
        lblAltaPriorita.setText("0");
        barChartWorkload.getData().clear();
        pieChartStato.getData().clear();
        pieChartPriorita.getData().clear();
        listUtentiReport.getChildren().clear();
        topPerformerBox.getChildren().clear();

        Label vuoto = new Label("Nessun dato disponibile per il periodo selezionato.");
        vuoto.getStyleClass().add("label-muted");
        listUtentiReport.getChildren().add(vuoto);
    }

    // ════════════════════════════════════════════════════════════════════════
    // ESPORTA PDF  (da implementare)
    // ════════════════════════════════════════════════════════════════════════
    @FXML
    private void onEsportaClicked() {
        // TODO: generare PDF con JasperReports o iText
    }

    // ════════════════════════════════════════════════════════════════════════
    // UTILITY DI PARSING E COMFORT
    // ════════════════════════════════════════════════════════════════════════

    private int estraiAnno(String voceCombo) {
        try {
            String[] parts = voceCombo.split(" ");
            return Integer.parseInt(parts[1]);
        } catch (Exception e) {
            return 2026; // Fallback di sicurezza se la stringa fallisce il parsing
        }
    }

    private int estraiMese(String voceCombo) {
        String nomeMese = voceCombo.split(" ")[0].toLowerCase();
        return switch (nomeMese) {
            case "gennaio"   -> 1;
            case "febbraio"  -> 2;
            case "marzo"     -> 3;
            case "aprile"    -> 4;
            case "maggio"    -> 5;
            case "giugno"    -> 6;
            case "luglio"    -> 7;
            case "agosto"    -> 8;
            case "settembre" -> 9;
            case "ottobre"   -> 10;
            case "novembre"  -> 11;
            case "dicembre"  -> 12;
            default          -> 1;
        };
    }

    /** Valore intero safe da null. */
    private int nullSafe(Integer val) {
        return val != null ? val : 0;
    }

    /** Iniziali da nome e cognome. */
    private String iniziali(String nome, String cognome) {
        String n = (nome    != null && !nome.isEmpty())    ? nome.substring(0, 1).toUpperCase()    : "";
        String c = (cognome != null && !cognome.isEmpty()) ? cognome.substring(0, 1).toUpperCase() : "";
        return n + c;
    }

    /** Colore avatar deterministico basato sul nome. */
    private String coloreAvatar(String nome) {
        String[] colori = {
                "avatar-blue", "avatar-green", "avatar-orange",
                "avatar-red",  "avatar-purple", "avatar-teal"
        };
        if (nome == null || nome.isEmpty()) return colori[0];
        return colori[Math.abs(nome.hashCode()) % colori.length];
    }

    /** Label numerica per le colonne della tabella. */
    private Label tdNum(Integer val, double prefWidth) {
        Label l = new Label(val != null ? String.valueOf(val) : "—");
        l.getStyleClass().add("report-td");
        l.setPrefWidth(prefWidth);
        return l;
    }

    /** Tooltip su ogni fetta del PieChart. */
    private void applicaTooltipPie(PieChart chart) {
        chart.getData().forEach(d ->
                Tooltip.install(d.getNode(),
                        new Tooltip(d.getName() + "\n" + (int) d.getPieValue() + " issue"))
        );
    }

    private String cercaNomeCompleto(List<UserReportDTO> report, String etichetta) {
        return report.stream()
                .filter(r -> iniziali(r.firstName(), r.lastName()).equals(etichetta))
                .map(r -> r.firstName() + " " + r.lastName())
                .findFirst()
                .orElse(etichetta);
    }

    private List<String> generaUltimiMesi(int quantiMesi) {
        List<String> lista = new ArrayList<>();
        LocalDate dataCorrente = LocalDate.now();

        // Formatto in italiano
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ITALIAN);

        for (int i = 0; i < quantiMesi; i++) {
            String meseFormattato = dataCorrente.minusMonths(i).format(formatter);
            meseFormattato = meseFormattato.substring(0, 1).toUpperCase() + meseFormattato.substring(1);
            lista.add(meseFormattato);
        }
        return lista;
    }
}