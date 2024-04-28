package com.crud.Reportes;

import com.crud.Database;
import com.crud.Login.LoginData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetDataReportes {

    static Connection conexion;
    static PreparedStatement preparedStatement;
    static ResultSet resultSet;

    public static ObservableList<LoginData> getDataReportes(){
        ObservableList<LoginData> data = FXCollections.observableArrayList();
        String consulta = "SELECT * FROM usuarios INNER JOIN auditoria_login ON usuarios.id = auditoria_login.id;";
        try {
            LoginData lData;
            conexion = Database.connectionDB();
            preparedStatement = conexion.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lData = new LoginData(resultSet.getInt("id"), resultSet.getString("nombre"), resultSet.getDate("fecha"), resultSet.getInt("cantidad"));
                data.add(lData);
            }
        }catch (Exception e) { e.printStackTrace(); }
        return data;
    }

    public static ObservableList<TableReporte> dataTableReportes(){
        ObservableList<TableReporte> data = FXCollections.observableArrayList();
        String consulta = "SELECT usuarios.nombre, SUM(auditoria_login.cantidad) AS conexiones FROM auditoria_login JOIN usuarios ON auditoria_login.id = usuarios.id GROUP BY usuarios.id ORDER BY cantidad DESC LIMIT 5;";
        TableReporte tReporte;
        try {
            conexion = Database.connectionDB();
            preparedStatement = conexion.prepareStatement(consulta);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tReporte = new TableReporte(resultSet.getString("nombre"), resultSet.getInt("conexiones"));
                data.add(tReporte);
            }

        }catch (Exception e) { e.printStackTrace(); }
        return data;
    }




}
