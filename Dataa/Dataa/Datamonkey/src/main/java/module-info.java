module integradora.java.datamonkey {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;

    // FXML y Application
    opens mx.edu.utez.datamonkey to javafx.graphics, javafx.fxml;
    opens mx.edu.utez.datamonkey.controller to javafx.fxml;

    // Para PropertyValueFactory en TableView (Provider, Product, etc.)
    opens mx.edu.utez.datamonkey.model to javafx.base;

    exports mx.edu.utez.datamonkey;
    exports mx.edu.utez.datamonkey.controller;
}
