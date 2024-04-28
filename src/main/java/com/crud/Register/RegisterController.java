package com.crud.Register;

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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import static com.crud.Utils.Hooks.createStage;
import static com.crud.Utils.Hooks.hideStage;

public class RegisterController implements Initializable {

    public Button to_loginForm;
    public PasswordField su_password;
    public Button su_btn_register;
    public TextField su_nombre;
    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        su_btn_register.setOnAction(event -> registrarUsuario());
    }

    public void registrarUsuario(){
        String varificarUsuario = "SELECT * FROM usuarios WHERE nombre = ? and password = ?;";
        connection = Database.connectionDB();
        try {
            Alert alert;
            preparedStatement =  connection.prepareStatement(varificarUsuario);
            preparedStatement.setString(1,su_nombre.getText());
            preparedStatement.setString(2,su_password.getText());
            resultSet =preparedStatement.executeQuery();

            if (resultSet.next()) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("El usuario existe en la base de datos");
                alert.showAndWait();
                return;
            }

            if(su_nombre.getText().isEmpty() || su_password.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Llena todos los campos.");
                alert.showAndWait();
                return;
            }

            String insertDb = "INSERT INTO usuarios (nombre,password) VALUES (?,?);";
            preparedStatement = connection.prepareStatement(insertDb);
            preparedStatement.setString(1, su_nombre.getText());
            preparedStatement.setString(2, su_password.getText());
            preparedStatement.executeUpdate();
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Registro");
            alert.setHeaderText(null);
            alert.setContentText("Registro exitoso");
            alert.showAndWait();
            limpiarFormulario();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void limpiarFormulario (){
        su_password.clear();
        su_nombre.clear();
    }

    public void toLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Inicio/Login.fxml"));
        hideStage(to_loginForm);
        createStage(loader);
    }
}
