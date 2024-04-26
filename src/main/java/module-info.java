module com.crud {
    requires javafx.controls;
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires lombok;

    opens com.crud to javafx.fxml;
    exports com.crud;
    exports com.crud.Inicio;
    exports com.crud.Utils;
    exports com.crud.Login;
    exports com.crud.Register;
    exports com.crud.Reportes;
}