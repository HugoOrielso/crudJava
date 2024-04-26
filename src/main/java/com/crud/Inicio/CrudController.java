package com.crud.Inicio;

import com.crud.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.crud.Utils.Hooks.createStage;
import static com.crud.Utils.Hooks.hideStage;

public class CrudController implements Initializable {

    public TextField numero_worker;
    public TextField name_worker;
    public ComboBox seccion_worker;
    public ComboBox genero_worker;
    public Button btn_agregar;
    public Button btn_actualizar;
    public Button btn_limpiar;
    public Button btn_eliminar;
    public TableView<WorkerData> crud_table;
    public TableColumn<WorkerData, String> col_number;
    public TableColumn<WorkerData, String> col_name;
    public TableColumn<WorkerData, String> col_year;
    public TableColumn<WorkerData, String> col_seccion;
    public TableColumn<WorkerData, String> col_genero;
    public Button btn_reportes;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private Statement statement;
    private ResultSet resultSet;
    private Alert alert;
    private String[] listTiposDeTrabajo = {"Programador", "Cocinero", "Estilista"};

    public void trabajadoresTipos(){
        List<String> tList = new ArrayList<>();

        for(String data:listTiposDeTrabajo){
            tList.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(tList);
        seccion_worker.setItems(listData);

    }
    private String[] generoList = {"Masculino", "Femenino"};
    public void generosTipos(){
        List<String> gList = new ArrayList<>();

        for(String data:generoList){
            gList.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(gList);
        genero_worker.setItems(listData);

    }
    public ObservableList<WorkerData> workerDataList(){
        ObservableList<WorkerData> listData = FXCollections.observableArrayList();
        String selectData = "SELECT * FROM trabajadores;";
        connection = Database.connectionDB();
        try {
            preparedStatement = connection.prepareStatement(selectData);
            resultSet = preparedStatement.executeQuery();
            WorkerData wData;
            while (resultSet.next()){
                wData = new WorkerData(resultSet.getInt("numero"), resultSet.getString("nombre_completo"),resultSet.getString("seccion"), resultSet.getString("fecha"),resultSet.getString("genero"));
                listData.add(wData);
            }
        }catch (Exception e){e.printStackTrace();}
        return listData;
    }

    private ObservableList<WorkerData> allDataWorker;
    public void workerShowData(){
        allDataWorker = workerDataList();
        col_number.setCellValueFactory(new PropertyValueFactory<>("numberWorker"));
        col_name.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        col_year.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        col_seccion.setCellValueFactory(new PropertyValueFactory<>("seccion"));
        col_genero.setCellValueFactory(new PropertyValueFactory<>("genero"));
        crud_table.setItems(allDataWorker);
    }

    public void checkId(String id){
        if (id == null || id.isEmpty()){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("El id no puede estar vacio");
            alert.showAndWait();
            return;
        }

    }

    public void toReportes(){
        hideStage(btn_reportes);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Inicio/Reportes.fxml"));
        createStage(loader);
    }

    public void workerSelectData(){
        WorkerData wData = crud_table.getSelectionModel().getSelectedItem();
        System.out.println(wData);
        if (wData == null) {
            return;
        }
        int num = crud_table.getSelectionModel().getSelectedIndex();
        if((num - 1 ) < 1){
            return;
        }
        numero_worker.setText(String.valueOf(wData.getNumberWorker()));
        name_worker.setText(wData.getNombreCompleto());
        seccion_worker.setValue(wData.getSeccion());
        genero_worker.setValue(wData.getGenero());
    }

    public void workerAddBtn(){
        String insertDb = "INSERT INTO trabajadores (numero,nombre_completo,seccion,genero) VALUES (?,?,?,?);";
        connection = Database.connectionDB();
        try {
            if(numero_worker.getText().isEmpty() || name_worker.getText().isEmpty() || seccion_worker.getSelectionModel().getSelectedItem() == null || genero_worker.getSelectionModel().getSelectedItem() == null){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Completa todos los campos.");
                alert.showAndWait();
            }else{
                String chekWorker = "SELECT nombre_completo FROM trabajadores WHERE numero = ?;";
                preparedStatement = connection.prepareStatement(chekWorker);
                preparedStatement.setString(1, numero_worker.getText());
                resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("El trabajador ya existe");
                    alert.showAndWait();
                }else{
                    preparedStatement = connection.prepareStatement(insertDb);
                    preparedStatement.setString(1,numero_worker.getText());
                    preparedStatement.setString(2,name_worker.getText());
                    preparedStatement.setString(3,(String)seccion_worker.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(4,(String)genero_worker.getSelectionModel().getSelectedItem());
                    preparedStatement.executeUpdate();
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Trabajador agregado exitósamente");
                    alert.showAndWait();
                    workerShowData();
                    limpiarBtn();
                }
            }
        }catch (Exception e){ e.printStackTrace();}
    }

    public void actualizarTrabajador(){
        if (numero_worker.getText().isEmpty() || name_worker.getText().isEmpty() || seccion_worker.getSelectionModel().getSelectedItem() == null || genero_worker.getSelectionModel().getSelectedItem() == null) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Completa todos los campos.");
            alert.showAndWait();
            return;
        }
        connection = Database.connectionDB();
        try {

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mensaje de confirmación");
                alert.setHeaderText(null);
                alert.setContentText("¿Estás seguro que quieres actualizar los campos?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    String updateData = "UPDATE trabajadores SET nombre_completo = ?, seccion = ? , genero = ? WHERE numero = ?;";
                    preparedStatement = connection.prepareStatement(updateData);
                    preparedStatement.setString(1,name_worker.getText());
                    preparedStatement.setString(2,(String)seccion_worker.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(3,(String)genero_worker.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(4,numero_worker.getText());
                    preparedStatement.executeUpdate();
                    workerShowData();
                    limpiarBtn();
                }else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Mensaje informativo");
                    alert.setHeaderText(null);
                    alert.setContentText("cambio cancelados");
                    alert.showAndWait();
                }
        }catch (Exception e){e.printStackTrace();}
    }

    public void eliminarTrabajador(){
        connection = Database.connectionDB();
        try {
            if(numero_worker.getText().isEmpty() ){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Completa todos los campos.");
                alert.showAndWait();

            }else{
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Mensaje de confirmación");
                alert.setHeaderText(null);
                alert.setContentText("¿Estás seguro que quieres eliminar este usuario?");
                Optional<ButtonType> option = alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    String deleteData = "DELETE FROM trabajadores WHERE numero = ?;";
                    preparedStatement = connection.prepareStatement(deleteData);
                    preparedStatement.setString(1,numero_worker.getText());
                    preparedStatement.executeUpdate();
                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Mensaje informativo");
                    alert.setHeaderText(null);
                    alert.setContentText("Eliminado correctamente");
                    alert.showAndWait();
                    workerShowData();
                    limpiarBtn();
                }else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Mensaje informativo");
                    alert.setHeaderText(null);
                    alert.setContentText("cambio cancelados");
                    alert.showAndWait();
                }
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public void limpiarBtn(){
        numero_worker.clear();
        name_worker.clear();
        seccion_worker.getSelectionModel().clearSelection();
        genero_worker.getSelectionModel().clearSelection();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        workerShowData();
        generosTipos();
        trabajadoresTipos();
        btn_actualizar.setOnAction(event -> actualizarTrabajador());
        btn_agregar.setOnAction(event -> workerAddBtn());
        btn_eliminar.setOnAction(event -> eliminarTrabajador());
        btn_limpiar.setOnAction(event -> limpiarBtn());
        btn_reportes.setOnAction(event -> toReportes());
    }
}
