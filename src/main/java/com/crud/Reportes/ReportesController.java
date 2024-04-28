package com.crud.Reportes;

import com.crud.Database;
import com.crud.Login.LoginData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import static com.crud.Reportes.GenerarExcel.generarExcel;
import static com.crud.Reportes.GenerarPDF.generarPdf;
import static com.crud.Reportes.GetDataReportes.dataTableReportes;
import static com.crud.Utils.Hooks.createStage;
import static com.crud.Utils.Hooks.hideStage;

public class ReportesController implements Initializable {

    public TableView tableReportes;
    public TableColumn col_re_fecha;
    public TableColumn col_re_conexiones;
    public TableColumn col_re_nombre;
    public Button btn_volver;
    public Button btn_pdf;
    public Button btn_excel;
    public BarChart<String, Integer> chartDataTable;
    Connection connection;
    PreparedStatement preparedStatement;
    ResultSet resultSet;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btn_volver.setOnAction(event -> toInicio());
        showData();
        topTable();
    }

    public void toInicio(){
        hideStage(btn_volver);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Inicio/Crud.fxml"));
        createStage(loader);
    }

    public ObservableList<LoginData> obtenerEstadisticas(){
        ObservableList<LoginData> data = FXCollections.observableArrayList();
        String consulta = "SELECT * FROM auditoria_login INNER JOIN usuarios ON usuarios.id = auditoria_login.id;";
        connection = Database.connectionDB();
        try {
            LoginData lData;
            preparedStatement = connection.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lData = new LoginData(resultSet.getInt("id"), resultSet.getString("nombre"), resultSet.getDate("fecha"), resultSet.getInt("cantidad"));
                data.add(lData);

            }
        }catch (Exception e){ e.printStackTrace(); }
        return data;
    }

    private ObservableList<LoginData> estadisticas;
    public void showData(){
        estadisticas = obtenerEstadisticas();
        col_re_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_re_fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        col_re_conexiones.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        tableReportes.setItems(estadisticas);
        btn_pdf.setOnAction(event -> generarPdf());
        btn_excel.setOnAction(event -> generarExcel());
    }

    public static ObservableList<TableReporte> tableEstadisticas;
    public void topTable(){
        tableEstadisticas = dataTableReportes();
        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Estadisticas");
        for (TableReporte tEstadistica : tableEstadisticas){
            series.getData().add(new XYChart.Data<>(tEstadistica.getNombre(), tEstadistica.getConexiones()));
        }
        chartDataTable.getData().add(series);
    }

}
