package com.example.parqueoscallejeros.Reportes;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReservaDao {
    private final String DB_URL = "jdbc:sqlite:databases/parqueoscallejeros.db";

    /**
     * este metodo muestra todas las multas en un periodo de tiempo
     * @param fechaInicio
     * @param fechaFin
     * @return una lista con las reservas
     */
    public List<Reserva> mostrarReserva(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT * FROM Reservas";

        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }

        // Depuración
        System.out.println("Fecha Inicio: " + fechaInicio);
        System.out.println("Fecha Fin: " + fechaFin);

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Usa la consulta corregida

            // Establecer las fechas en el PreparedStatement

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int idUsuario = rs.getInt("id_usuario");
                int idEspacio = rs.getInt("id_espacio");
                String placa = rs.getString("placa");
                int tiempoReserva = rs.getInt("tiempo_reservado");
                int costo = rs.getInt("costo");
                Timestamp fechaReserva = rs.getTimestamp("fecha_reserva");
                // Verificar si la fecha de la multa está dentro del rango
                if (fechaReserva.toLocalDateTime().isAfter(fechaInicio) && fechaReserva.toLocalDateTime().isBefore(fechaFin.plusNanos(1))) {
                    Reserva reserva = new Reserva(id, idUsuario, idEspacio, placa, tiempoReserva, costo, fechaReserva);
                    reservas.add(reserva);

                    // Imprimir cada multa encontrada
                    System.out.println("Id: " + id + " Id usuario: "+ idUsuario+ "id espacio: "+idEspacio);
                }
            }

            System.out.println("Total de reservas encontradas: " + reservas.size());

        } catch (SQLException e) {
            System.err.println("Error al mostrar las reservas: " + e.getMessage());
            e.printStackTrace();
        }

        return reservas;
    }


}
