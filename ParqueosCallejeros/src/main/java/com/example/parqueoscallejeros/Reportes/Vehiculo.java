package com.example.parqueoscallejeros.Reportes;

public class Vehiculo {
    private int idVehiculo;
    private String placa;
    private String marca;
    private String modelo;
    private int idUsuario;
    private int parqueado;

    public Vehiculo(int idVehiculo, String placa, String marca, String modelo, int idUsuario, int parqueado) {
        this.idVehiculo = idVehiculo;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.idUsuario = idUsuario;
        this.parqueado = 0;
    }
    public int getIdVehiculo() {
        return idVehiculo;
    }
    public String getPlaca() {
        return placa;
    }

    public int getIdUsuario() {
        return idUsuario;
    }
    public String getMarca() {
        return marca;
    }
    public String getModelo() {
        return modelo;
    }
    public int getParqueado() {
        return parqueado;
    }
}
