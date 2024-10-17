package com.example.parqueoscallejeros.Reportes;

public class EspaciosParqueo {
    private int idParqueo;
    private int numeroEspacio;
    private int estado;

    public EspaciosParqueo(int idParqueo, int numeroEspacio, int estado) {
        this.idParqueo = idParqueo;
        this.numeroEspacio = numeroEspacio;
        this.estado = estado;
    }
    public int getIdParqueo() {
        return idParqueo;
    }

    public int getNumeroEspacio() {
        return numeroEspacio;
    }

    public int getEstado() {
        return estado;
    }
}
