package com.crud.Login;

import lombok.Getter;

import java.util.Date;

public class LoginData {
    @Getter
    public Integer id;
    @Getter
    public String nombre;
    @Getter
    public Date fecha;


    public LoginData(Integer id, String nombre, Date fecha) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
    }

}
