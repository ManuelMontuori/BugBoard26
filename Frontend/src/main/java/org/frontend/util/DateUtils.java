package org.frontend.util;

import javafx.scene.control.TableCell;
import org.frontend.models.MeseOpzione;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DateUtils {

    // ════════════════════════════════════════════════════════════════════════
    // FORMATTERS STATICI (Riutilizzabili)
    // ════════════════════════════════════════════════════════════════════════

    // Il formatter che usiamo per le notifiche e altre stringhe ISO
    private static final DateTimeFormatter ISO_TO_LOCAL_FMT = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    // ════════════════════════════════════════════════════════════════════════
    // METODI PER LE STRINGHE / ISO
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Converte una stringa ISO (es. dal database/backend) in formato leggibile.
     */
    public static String formatIsoToLocal(String isoDate) {
        if (isoDate == null || isoDate.isBlank()) return "";
        try {
            return ISO_TO_LOCAL_FMT.format(Instant.parse(isoDate));
        } catch (Exception e) {
            return isoDate; // Fallback se la stringa non è valida
        }
    }

    // ════════════════════════════════════════════════════════════════════════
    // METODI PREESISTENTI (Lista Mesi e Celle Tabella)
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Genera una lista di oggetti MeseOpzione per gli ultimi X mesi a partire da oggi.
     */
    public static List<MeseOpzione> generaUltimiMesi(int quantiMesi) {
        List<MeseOpzione> lista = new ArrayList<>();
        LocalDate dataCorrente = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ITALIAN);

        for (int i = 0; i < quantiMesi; i++) {
            LocalDate dataMese = dataCorrente.minusMonths(i);

            // Formatta la stringa (es: "giugno 2026") e rende la prima lettera maiuscola
            String label = dataMese.format(formatter);
            label = label.substring(0, 1).toUpperCase() + label.substring(1);

            // Crea l'oggetto pulito
            lista.add(new MeseOpzione(dataMese.getYear(), dataMese.getMonthValue(), label));
        }
        return lista;
    }

    /**
     * Crea una cella per le TableView di JavaFX per formattare LocalDateTime.
     */
    public static <T> TableCell<T, LocalDateTime> createDateCell(DateTimeFormatter formatter) {
        return new TableCell<>() {
            @Override
            protected void updateItem(LocalDateTime item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("-");
                    setGraphic(null);
                } else {
                    setText(formatter.format(item));
                    setGraphic(null);
                }
            }
        };
    }
}