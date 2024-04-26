package com.crud.Inicio;

import java.util.Date;

public class WorkerData {
    private Integer numberWorker;
    private String nombreCompleto;
    private String seccion;

    private String fecha;
    private String genero;
    public WorkerData(Integer numberWorker, String nombreCompleto, String seccion, String fecha, String genero) {
        this.numberWorker = numberWorker;
        this.nombreCompleto = nombreCompleto;
        this.seccion = seccion;
        this.fecha = fecha;
        this.genero = genero;
    }
    public Integer getNumberWorker() {
        return numberWorker;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public String getFecha() {
        return fecha;
    }
    public String getSeccion() {
        return seccion;
    }
    public String getGenero() {
        return genero;
    }
}
