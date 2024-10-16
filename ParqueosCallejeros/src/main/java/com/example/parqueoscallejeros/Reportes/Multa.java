package com.example.parqueoscallejeros.Reportes;

import java.util.Date;

import java.util.Date;

public class Multa {
    private int id;
    private int idInspector;
    private String placa;
    private double costo;
    private Date fechaMultado;

    public Multa(int id, int idInspector, String placa, double costo, Date fechaMultado) {
        this.id = id;
        this.idInspector = idInspector;
        this.placa = placa;
        this.costo = costo;
        this.fechaMultado = fechaMultado;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getIdInspector() {
        return idInspector;
    }

    public String getPlaca() {
        return placa;
    }

    public double getCosto() {
        return costo;
    }

    public Date getFechaMultado() {
        return fechaMultado;
    }
}