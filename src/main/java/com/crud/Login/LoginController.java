package com.crud.Login;

import com.crud.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.crud.Utils.Hooks.createStage;
import static com.crud.Utils.Hooks.hideStage;

public class LoginController implements Initializable {

    public Button to_login_form;
    public PasswordField lo_password;
    public Button lo_btn_login;
    public TextField lo_nombre;
    private Stage stage;
    private Scene scene;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lo_btn_login.setOnAction(event -> loginAccount());
    }

    private void loginAccount(){
        String consulta = "SELECT nombre, password, id FROM usuarios WHERE nombre = ? AND password = ?;";
        connection = Database.connectionDB();
        try {
            Alert alert;
            if(lo_nombre.getText().isEmpty() || lo_password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Llena todos los campos.");
                alert.showAndWait();
                return;
            }
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setString(1, lo_nombre.getText());
            preparedStatement.setString(2, lo_password.getText());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Información");
                alert.setHeaderText(null);
                alert.setContentText("Login exitoso");
                alert.showAndWait();
                int id = resultSet.getInt("id");
                verificarExistenciaEnAuditoria(id);
                lo_btn_login.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Inicio/Crud.fxml"));
                Stage stageCrud = new Stage();
                Scene sceneCrud = new Scene(root);
                stageCrud.setScene(sceneCrud);
                stageCrud.show();
                return;
            }
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Credenciales erróneas");
            alert.showAndWait();
            limpiarFormulario();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void limpiarFormulario(){
        lo_password.clear();
        lo_nombre.clear();
    }

    public void verificarExistenciaEnAuditoria(int id){
        String consulta = "SELECT * FROM auditoria_login WHERE id  = ? && fecha = ?;";
        String localDate = LocalDate.now().toString();
        try {
            preparedStatement = connection.prepareStatement(consulta);
            preparedStatement.setInt(1, id);
            preparedStatement.setString(2, localDate);
            resultSet = preparedStatement.executeQuery();
            if(!resultSet.next()){
                String insertData = "INSERT INTO auditoria_login (id, cantidad) VALUES (?,?);";
                preparedStatement = connection.prepareStatement(insertData);
                preparedStatement.setInt(1, id);
                preparedStatement.setInt(2, 1);
                preparedStatement.executeUpdate();
                return ;
            }
            String fechaDb = resultSet.getDate("fecha").toString();
            if(fechaDb.equals(localDate)){
                int cantidadActual = resultSet.getInt("cantidad");
                cantidadActual = cantidadActual + 1;
                String actualizarCantidad = "UPDATE auditoria_login SET cantidad = ? WHERE id = ? && fecha = ?;";
                preparedStatement = connection.prepareStatement(actualizarCantidad);
                preparedStatement.setInt(1, cantidadActual);
                preparedStatement.setInt(2, id);
                preparedStatement.setDate(3, Date.valueOf((fechaDb)));
                preparedStatement.executeUpdate();
                return;
            }
            String nuevoRegistro = "INSERT INTO auditoria_login (id, cantidad) VALUES (?,?);";
            preparedStatement = connection.prepareStatement(nuevoRegistro);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, 1);
            preparedStatement.executeUpdate();
        }catch (Exception e) { e.printStackTrace(); }
    }

    public void toRegister() throws IOException {
        FXMLLoader loader = new  FXMLLoader(getClass().getResource("/Fxml/Inicio/Registro.fxml"));
        hideStage(to_login_form);
        createStage(loader);
    }
}
