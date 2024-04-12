module com.crud {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;

    opens com.crud to javafx.fxml;
    exports com.crud;
    exports com.crud.Inicio;
}