package org.frontend.viewsControllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.frontend.controllers.DashboardController;
import org.frontend.models.Issue;
import org.frontend.services.AuthSession;


public class HomeDashboardController {


    @FXML
    private TableView<Issue> tblIssueRecenti;


    @FXML
    private TableColumn<Issue,String> colTitle;


    @FXML
    private TableColumn<Issue,String> colType;


    @FXML
    private TableColumn<Issue,String> colPriority;


    @FXML
    private TableColumn<Issue,String> colState;

    @FXML
    private TableColumn<Issue,String> colCreatedAt;

    @FXML
    private Label labelWelcome;



    private DashboardController controller;



    @FXML
    public void initialize(){


        controller =
                new DashboardController();


        String email = AuthSession.getInstance().getEmail();
        labelWelcome.setText(email.isEmpty() ? "Utente" : email);




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
        colType.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setGraphic(null); return; }

                Label badge = new Label(val);
                String normalized = val.toLowerCase().replace("_", "-");
                badge.getStyleClass().addAll("badge", "badge-" + normalized);
                setGraphic(badge);
                setText(null);
            }
        });



        colPriority.setCellValueFactory(
                data ->
                        data.getValue()
                                .priorityProperty()
        );
        colPriority.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setGraphic(null); return; }

                Label badge = new Label(val);
                String normalized = val.toLowerCase().replace("_", "-");
                badge.getStyleClass().addAll("badge", "badge-" + normalized);
                setGraphic(badge);
                setText(null);
            }
        });




        colState.setCellValueFactory(
                data ->
                        data.getValue()
                                .statusProperty()
        );
        colState.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String val, boolean empty) {
                super.updateItem(val, empty);
                if (empty || val == null) { setGraphic(null); return; }

                Label badge = new Label(val);
                String normalized = val.toLowerCase().replace("_", "-");
                badge.getStyleClass().addAll("badge", "badge-" + normalized);
                setGraphic(badge);
                setText(null);
            }
        });

        colCreatedAt.setCellValueFactory(
                data ->
                        data.getValue()
                                .createdAtProperty().asString()
        );


        controller.loadIssues();



        tblIssueRecenti.setItems(
                controller.getIssues()
        );

    }

}