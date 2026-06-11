module org.prova.provafrontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.httpserver;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires java.desktop;

    opens org.frontend to javafx.fxml;
    exports org.frontend;
    exports org.frontend.services.Auth;
    opens org.frontend.services.Auth to javafx.fxml;
    exports org.frontend.services;
    opens org.frontend.services to javafx.fxml;
    exports org.frontend.models;
    opens org.frontend.models to javafx.fxml;
    exports org.frontend.util;
    opens org.frontend.util to javafx.fxml;
    exports org.frontend.views;
    opens org.frontend.views to javafx.fxml;
}