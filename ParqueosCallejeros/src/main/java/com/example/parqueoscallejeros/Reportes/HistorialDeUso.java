package com.example.parqueoscallejeros.Reportes;


import java.util.Date;

public class HistorialDeUso {
    private int id;
    private int idUsuario;
    private int idEspacio;
    private int costo;
    private int tiempoOcupado;
    private Date fechaUso;

    public HistorialDeUso(int id, int idUsuario, int idEspacio, int costo, int tiempoOcupado, Date fechaUso) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idEspacio = idEspacio;
        this.costo = costo;
        this.fechaUso = fechaUso;
    }
    public int getId() {
        return id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdEspacio() {
        return idEspacio;
    }

    public int getCosto() {
        return costo;
    }

    public int getTiempoOcupado() {
        return tiempoOcupado;
    }

    public Date getFechaUso() {
        return fechaUso;
    }

}
