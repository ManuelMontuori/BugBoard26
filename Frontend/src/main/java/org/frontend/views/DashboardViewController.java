package org.frontend.views;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DashboardViewController {
    @FXML
    private Label dashboardLabel;

    @FXML
    private Button button1;

    @FXML
    private Label label1;


    public void setLabel1(String nome) {
        label1.setText(nome);
    }


}
