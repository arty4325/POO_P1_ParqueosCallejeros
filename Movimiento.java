import java.util.Date;

public class Movimiento {
    private Vehiculo vehiculo;
    private Date entrada;
    private Date salida;
    //private double monto;

    public Movimiento(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
        entrada = new Date();
    }

    public void setSalida() {
        if (salida == null)
            this.salida = new Date();
    }

}
