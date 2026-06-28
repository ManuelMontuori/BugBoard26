package org.frontend.util;

import javafx.scene.control.*;
import org.frontend.models.Issue;

public class IssueUI {

    /**
     * Crea una ListCell per le ComboBox che visualizza le Issue.
     * Centralizza la formattazione testuale per evitare duplicazioni.
     */
    public static ListCell<Issue> createIssueListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(Issue issue, boolean empty) {
                super.updateItem(issue, empty);
                if (empty || issue == null) {
                    setText(null);
                } else {
                    String uuidShort = issue.getUuid() != null ? issue.getUuid().substring(0, 8) : "???";
                    setText("#" + uuidShort + " — " + issue.getTitle());
                }
            }
        };
    }


}