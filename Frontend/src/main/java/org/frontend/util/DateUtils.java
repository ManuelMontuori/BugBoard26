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

    // Il formatter che usiamo per le notifiche
    private static final DateTimeFormatter ISO_TO_LOCAL_FMT = DateTimeFormatter
            .ofPattern("dd/MM/yyyy HH:mm")
            .withZone(ZoneId.systemDefault());

    public static String formatIsoToLocal(String date) {
        if (date == null || date.isBlank()) return "";
        try {
            return ISO_TO_LOCAL_FMT.format(Instant.parse(date));
        } catch (Exception e) {
            return date;
        }
    }

    public static List<MeseOpzione> generaUltimiMesi(int nMonunth) {
        List<MeseOpzione> listMounth = new ArrayList<>();
        LocalDate currentData = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ITALIAN);

        for (int i = 0; i < nMonunth; i++) {
            LocalDate dataMese = currentData.minusMonths(i);

            // Formatta la stringa (tipo "giugno 2026") e rende la prima lettera maiuscola
            String label = dataMese.format(formatter);
            label = label.substring(0, 1).toUpperCase() + label.substring(1);

            listMounth.add(new MeseOpzione(dataMese.getYear(), dataMese.getMonthValue(), label));
        }
        return listMounth;
    }

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