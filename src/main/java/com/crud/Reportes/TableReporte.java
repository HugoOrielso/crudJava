package com.crud.Reportes;

import lombok.Getter;

public class TableReporte {
    @Getter
    public String nombre;
    @Getter
    public Integer conexiones;
    public TableReporte(String nombre, Integer conexiones) {
        this.nombre = nombre;
        this.conexiones = conexiones;
    }
}
