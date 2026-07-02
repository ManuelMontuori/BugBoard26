package org.frontend.util;

import javafx.scene.control.*;
import org.frontend.models.Issue;

public class IssueUI {

    // crea una lista di issue visualizzate nella combobox di assegnaIssue
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