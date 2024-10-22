package com.example.parqueoscallejeros.Reportes;


import java.util.Date;

public class HistorialDeUso {
    private int id;
    private int idUsuario;
    private int idEspacio;
    private int costo;
    private int tiempoOcupado;
    private Date fechaUso;

    /**
     * Constructor del historial de uso
     * @param id id del usuario
     * @param idUsuario id alfanumerico del usuario
     * @param idEspacio id del espacio
     * @param costo costo del espacio
     * @param tiempoOcupado tiempo ocupado del espacio
     * @param fechaUso fecha de uso del espacio
     */
    public HistorialDeUso(int id, int idUsuario, int idEspacio, int costo, int tiempoOcupado, Date fechaUso) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.idEspacio = idEspacio;
        this.costo = costo;
        this.tiempoOcupado = tiempoOcupado;
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
