package com.example.parqueoscallejeros.Reportes;

import java.util.Date;

public class Reserva {
    private int idReserva;
    private int idUsuario;
    private int idEspacio;
    private String Placa;
    private int tiempoReserva;
    private int costo;
    private Date fecha;

    public Reserva(int idReserva, int idUsuario, int idEspacio, String Placa, int tiempoReserva, int costo, Date fecha) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.idEspacio = idEspacio;
        this.Placa = Placa;
        this.tiempoReserva = tiempoReserva;
        this.costo = costo;
        this.fecha = fecha;
    }
    public int getIdReserva() {
        return idReserva;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getIdEspacio() {
        return idEspacio;
    }

    public String getPlaca() {
        return Placa;
    }
    public int getTiempoReserva() {
        return tiempoReserva;
    }

    public int getCosto() {
        return costo;
    }

    public Date getFecha() {
        return fecha;
    }
}
