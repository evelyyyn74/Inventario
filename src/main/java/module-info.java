module integradora.java.datamonkey {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens mx.edu.utez.datamonkey.controller to javafx.fxml;
    opens mx.edu.utez.datamonkey to javafx.fxml; // opcional pero recomendado
    opens mx.edu.utez.datamonkey.model to javafx.base;

    exports mx.edu.utez.datamonkey;

}
