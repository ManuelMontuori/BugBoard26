package org.frontend.services;

import org.frontend.models.dtos.UserReportDTO;
import java.util.ArrayList;
import java.util.List;

public class ReportSummary {

    private int totAperte = 0;
    private int totRisolte = 0;
    private int altaPriorita = 0;
    private int totWorkload = 0;
    private int totCreate = 0;
    private double avgTempo = 0.0;
    private int maxWorkload = 1;

    private final List<UserReportDTO> ordinatiPerWorkload;
    private final List<UserReportDTO> topPerformer;

    public ReportSummary(List<UserReportDTO> reportGrezzo) {
        // 1. Calcoli metriche generali
        int utentiConTempoValido = 0;
        double sommaTempoRisoluzione = 0;

        for (UserReportDTO r : reportGrezzo) {
            this.totAperte += nullSafe(r.totIssue());
            this.totRisolte += nullSafe(r.totResolvedIssue());
            this.altaPriorita += nullSafe(r.totHighPriorityIssue());
            this.totWorkload += nullSafe(r.totWorkloadIssue());
            this.totCreate += nullSafe(r.totCreatedIssue());

            if (r.averageResolutionTime() != null) {
                sommaTempoRisoluzione += r.averageResolutionTime();
                utentiConTempoValido++;
            }

            if (nullSafe(r.totWorkloadIssue()) > this.maxWorkload) {
                this.maxWorkload = nullSafe(r.totWorkloadIssue());
            }
        }

        if (utentiConTempoValido > 0) {
            this.avgTempo = sommaTempoRisoluzione / utentiConTempoValido;
        }

        // 2. Ordinamento per Workload (Primi 10 per il BarChart e la Tabella)
        this.ordinatiPerWorkload = new ArrayList<>(reportGrezzo);
        this.ordinatiPerWorkload.sort((r1, r2) ->
                Integer.compare(nullSafe(r2.totWorkloadIssue()), nullSafe(r1.totWorkloadIssue()))
        );

        // 3. Ordinamento Top Performer (Primi 5 risolte)
        this.topPerformer = new ArrayList<>(reportGrezzo);
        this.topPerformer.sort((r1, r2) ->
                Integer.compare(nullSafe(r2.totResolvedIssue()), nullSafe(r1.totResolvedIssue()))
        );
    }

    // ── GETTER PER IL CONTROLLER ────────────────────────────────────────────
    public int getTotAperte() { return totAperte; }
    public int getTotRisolte() { return totRisolte; }
    public int getAltaPriorita() { return altaPriorita; }
    public int getTotWorkload() { return totWorkload; }
    public int getAltreAperte() { return Math.max(0, totCreate - totRisolte - totWorkload); }
    public double getAvgTempo() { return avgTempo; }
    public int getMaxWorkload() { return maxWorkload; }
    public int getPercentualeRisolte() { return totAperte > 0 ? (totRisolte * 100 / totAperte) : 0; }

    public List<UserReportDTO> getOrdinatiPerWorkload() { return ordinatiPerWorkload; }
    public List<UserReportDTO> getTopPerformer() { return topPerformer; }

    private int nullSafe(Integer val) {
        return val != null ? val : 0;
    }
}