package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.frontend.controllers.IssueController;
import org.frontend.models.Issue;

public class DashboardViewController {
    @FXML private TableView<Issue> tblIssueRecenti;
    @FXML private TableColumn<Issue,String> colTitle;
    @FXML private TableColumn<Issue,String> colType;
    @FXML private TableColumn<Issue,String> colPriority;
    @FXML private TableColumn<Issue,String> colState;

    private IssueController controller;

    @FXML
    public void initialize(){
        controller =
                new IssueController();

        colTitle.setCellValueFactory(
                data ->
                        data.getValue()
                                .titleProperty()
        );

        colType.setCellValueFactory(
                data ->
                        data.getValue()
                                .typeProperty()
        );

        colPriority.setCellValueFactory(
                data ->
                        data.getValue()
                                .priorityProperty()
        );

        colState.setCellValueFactory(
                data ->
                        data.getValue()
                                .statusProperty()
        );

        controller.loadIssues();

        tblIssueRecenti.setItems(
                controller.getIssues()
        );
    }
}