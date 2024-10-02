public class Espacio {
    private Vehiculo vehiculo;
    private boolean ocupado;

    public Espacio() {
        vehiculo = null;
        ocupado = false;
    }

    @Override
    public String toString() {
        String status = "Libre";
        if (ocupado) status = "Ocupado";

        if (vehiculo != null)
            return "Status: " + status + "     " + vehiculo.toString();
        return "Status: " + status;
    }


    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public boolean isOcupado() {
        return ocupado;
    }

    public void setOcupado(boolean ocupado) {
        this.ocupado = ocupado;
    }
}