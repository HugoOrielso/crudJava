package com.crud.Reportes;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

import static com.crud.Utils.Hooks.createStage;
import static com.crud.Utils.Hooks.hideStage;

public class ReportesController implements Initializable {

    public TableView tableReportes;
    public TableColumn col_re_fecha;
    public TableColumn col_re_conexiones;
    public TableColumn col_re_nombre;
    public Button brn_excel;
    public Button btn_volver;
    public Button btn_pdf;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_volver.setOnAction(event -> toInicio());
    }

    public void toInicio(){
        hideStage(btn_volver);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Inicio/Crud.fxml"));
        createStage(loader);
    }


}
