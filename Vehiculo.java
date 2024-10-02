import java.time.LocalDateTime;

public class Vehiculo {
    private String placa;
    private LocalDateTime fechaHora;

    public Vehiculo(String placa) {
        this.placa = placa;
        this.fechaHora = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return placa + "  Fecha y Hora: " + fechaHora;
    }

    public String getPlaca() {
        return placa;
    }

    public LocalDateTime getFechaHora() {
        return this.fechaHora;
    }

}