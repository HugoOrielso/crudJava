package com.crud;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    public static Connection connectionDB (){
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/crud_java","root", "");
                return connection;
            }catch ( Exception e){
                e.printStackTrace();
            }
            return null;
    }
}
