package org.frontend.viewsControllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.frontend.controllers.UserController;
import org.frontend.models.MeseOpzione;
import org.frontend.models.dtos.UserReportDTO;
import org.frontend.services.ReportRowService;
import org.frontend.services.ReportSummary;
import org.frontend.util.DateUtils;
import java.util.List;

public class ReportViewController {

    @FXML
    private ComboBox<MeseOpzione> comboMese;
    @FXML
    private Button btnEsporta;
    @FXML
    private Label lblTotAperte;
    @FXML
    private Label lblTotAperteDesc;
    @FXML
    private Label lblTotRisolte;
    @FXML
    private Label lblTotRisolteDesc;
    @FXML
    private Label lblAvgTempo;
    @FXML
    private Label lblAltaPriorita;
    @FXML
    private BarChart<String, Number> barChartWorkload;
    @FXML
    private PieChart pieChartStato;
    @FXML
    private PieChart pieChartPriorita;
    @FXML
    private VBox listUtentiReport;
    @FXML
    private VBox topPerformerBox;

    private UserController userController;
    private ReportRowService rowService;

    public void initDependencies(UserController userController, ReportRowService rowService) {
        this.userController = userController;
        this.rowService = rowService;

        comboMese.setItems(FXCollections.observableArrayList(DateUtils.generaUltimiMesi(12)));
        comboMese.getSelectionModel().selectFirst();
        caricaReport();
    }

    @FXML
    public void initialize() {

        comboMese.setOnAction(e -> caricaReport());
    }

    private void caricaReport() {

        if (userController == null || rowService == null) {
            return;
        }

        MeseOpzione opzione = comboMese.getValue();
        if (opzione == null) {
            mostraVuoto();
            return;
        }

        List<UserReportDTO> reportGrezzo = userController.getMonthlyReport(opzione.getAnno(), opzione.getMese());
        if (reportGrezzo == null || reportGrezzo.isEmpty()) {
            mostraVuoto();
            return;
        }

        ReportSummary summary = new ReportSummary(reportGrezzo);

        disegnaKPI(summary);
        disegnaBarChart(summary, reportGrezzo);
        disegnaPieCharts(summary);
        disegnaTabellaETopPerformer(summary);
    }

    private void disegnaKPI(ReportSummary summary) {
        lblTotAperte.setText(String.valueOf(summary.getTotAperte()));
        lblTotRisolte.setText(String.valueOf(summary.getTotRisolte()));
        lblAltaPriorita.setText(String.valueOf(summary.getAltaPriorita()));
        lblAvgTempo.setText(String.format("%.1f h", summary.getAvgTempo()));

        lblTotAperteDesc.setText("nel periodo selezionato");
        lblTotRisolteDesc.setText(summary.getPercentualeRisolte() + "% del totale");
    }

    private void disegnaBarChart(ReportSummary summary, List<UserReportDTO> reportGrezzo) {
        barChartWorkload.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Workload attivo");

        List<UserReportDTO> topWorkload = summary.getOrdinatiPerWorkload();
        int limite = Math.min(10, topWorkload.size());

        for (int i = 0; i < limite; i++) {
            UserReportDTO r = topWorkload.get(i);

            String label = rowService.calcolaIniziali(r.firstName(), r.lastName());
            series.getData().add(new XYChart.Data<>(label, r.totWorkloadIssue() != null ? r.totWorkloadIssue() : 0));
        }
        barChartWorkload.getData().add(series);

        for (XYChart.Data<String, Number> data : series.getData()) {
            String nomeCompleto = cercaNomeCompleto(reportGrezzo, data.getXValue());
            Tooltip.install(data.getNode(), new Tooltip(nomeCompleto + "\nWorkload: " + data.getYValue()));
        }
    }

    private void disegnaPieCharts(ReportSummary summary) {
        pieChartStato.getData().setAll(
                new PieChart.Data("Risolte (" + summary.getTotRisolte() + ")", summary.getTotRisolte()),
                new PieChart.Data("In lavoraz. (" + summary.getTotWorkload() + ")", summary.getTotWorkload()),
                new PieChart.Data("Altre aperte (" + summary.getAltreAperte() + ")", summary.getAltreAperte()));
                
        int altraPriorita = Math.max(0, summary.getTotAperte() - summary.getAltaPriorita());
        pieChartPriorita.getData().setAll(
                new PieChart.Data("Alta priorità (" + summary.getAltaPriorita() + ")", summary.getAltaPriorita()),
                new PieChart.Data("Normale (" + altraPriorita + ")", altraPriorita));

        applicaTooltipPie(pieChartStato);
        applicaTooltipPie(pieChartPriorita);
    }

    private void disegnaTabellaETopPerformer(ReportSummary summary) {
        listUtentiReport.getChildren().clear();
        topPerformerBox.getChildren().clear();

        for (UserReportDTO r : summary.getOrdinatiPerWorkload()) {
            listUtentiReport.getChildren().add(rowService.creaRigaUtente(r, summary.getMaxWorkload()));
        }

        String[] medaglie = { "🥇", "🥈", "🥉", "4.", "5." };
        String[] rankStyle = { "top-performer-rank-gold", "top-performer-rank-silver", "top-performer-rank-bronze",
                "top-performer-rank", "top-performer-rank" };

        List<UserReportDTO> top = summary.getTopPerformer();
        int limite = Math.min(5, top.size());
        for (int i = 0; i < limite; i++) {
            topPerformerBox.getChildren().add(rowService.creaRigaTop(top.get(i), medaglie[i], rankStyle[i]));
        }
    }

    private void mostraVuoto() {
        lblTotAperte.setText("0");
        lblTotRisolte.setText("0");
        lblAvgTempo.setText("— h");
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

    private void applicaTooltipPie(PieChart chart) {
        for (PieChart.Data d : chart.getData()) {
            Tooltip.install(d.getNode(), new Tooltip(d.getName() + "\n" + (int) d.getPieValue() + " issue"));
        }
    }

    private String cercaNomeCompleto(List<UserReportDTO> report, String etichetta) {
        for (UserReportDTO r : report) {
            if (rowService.calcolaIniziali(r.firstName(), r.lastName()).equals(etichetta)) {
                return r.firstName() + " " + r.lastName();
            }
        }
        return etichetta;
    }

}