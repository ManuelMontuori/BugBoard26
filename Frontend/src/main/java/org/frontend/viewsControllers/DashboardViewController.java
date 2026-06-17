package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.frontend.controllers.DashboardController;
import org.frontend.models.Issue;

public class DashboardViewController {
    @FXML private TableView<Issue> tblIssueRecenti;
    @FXML private TableColumn<Issue,String> colTitle;
    @FXML private TableColumn<Issue,String> colType;
    @FXML private TableColumn<Issue,String> colPriority;
    @FXML private TableColumn<Issue,String> colState;

    private DashboardController controller;

    @FXML
    public void initialize(){
        controller =
                new DashboardController();

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