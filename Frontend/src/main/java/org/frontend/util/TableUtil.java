package org.frontend.util;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;

public class TableUtil {

    // Interfaccia funzionale per definire l'azione del toggle che può lanciare eccezioni
    public interface ToggleAction<T> {
        void execute(T item, boolean isSelected) throws Exception;
    }

    // Genera una cella contenente una CheckBox interattiva.
    public static <T> TableCell<T, Boolean> createToggleSwitchCell(ToggleAction<T> action) {
        return new TableCell<>() {
            private final CheckBox cb = new CheckBox();

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    // setOnAction a null per evitare vecchie selezioni nella checkbox
                    cb.setOnAction(null);
                    cb.setSelected(item);
                    setGraphic(cb);

                    // lancio della nuova selezione
                    cb.setOnAction(e -> {
                        T rowData = getTableRow().getItem();
                        if (rowData != null) {
                            boolean selected = cb.isSelected();
                            try {
                                action.execute(rowData, selected);
                            } catch (Exception ex) {
                                // se la chiamata API fallisce, cancellazione della scelta fatta
                                cb.setSelected(!selected);
                                System.err.println("Impossibile aggiornare lo stato: " + ex.getMessage());
                            }
                        }
                    });
                }
            }
        };
    }
}