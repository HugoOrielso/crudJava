package com.crud.Inicio;

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
import java.util.PrimitiveIterator;
import java.util.ResourceBundle;

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
        String consulta = "SELECT nombre, password FROM usuarios WHERE nombre = ? AND password = ?;";
        connection = Database.connectionDB();

        try {
            Alert alert;
            if(lo_nombre.getText().isEmpty() || lo_password.getText().isEmpty()){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Llena todos los campos.");
                alert.showAndWait();
            }else{
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
                    lo_btn_login.getScene().getWindow().hide();
                    Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Inicio/Crud.fxml"));
                    Stage stageCrud = new Stage();
                    Scene sceneCrud = new Scene(root);
                    stageCrud.setScene(sceneCrud);
                    stageCrud.show();
                }else{
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Credenciales erróneas");
                    alert.showAndWait();
                    limpiarFormulario();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void limpiarFormulario(){
        lo_password.clear();
        lo_nombre.clear();
    }
    public void toRegister(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/Fxml/Inicio/Registro.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
