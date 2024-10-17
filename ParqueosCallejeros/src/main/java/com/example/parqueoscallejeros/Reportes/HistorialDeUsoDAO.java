package com.example.parqueoscallejeros.Reportes;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HistorialDeUsoDAO {
    private final String DB_URL = "jdbc:sqlite:databases/parqueoscallejeros.db";

    // Método para obtener todas las multas de la base de datos en un rango de tiempo
    public List<HistorialDeUso> mostrarHistorialUso(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<HistorialDeUso> historial = new ArrayList<>();
        String sql = "SELECT * FROM HistorialUso";

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
                int costo = rs.getInt("costo");
                int tiempoOcupado = rs.getInt("tiempo_ocupado");
                Timestamp fechaUso = rs.getTimestamp("fecha_uso");
                // Verificar si la fecha de la multa está dentro del rango
                if (fechaUso.toLocalDateTime().isAfter(fechaInicio) && fechaUso.toLocalDateTime().isBefore(fechaFin.plusNanos(1))) {
                    HistorialDeUso historialDeUso = new HistorialDeUso(id, idUsuario, idEspacio, costo, tiempoOcupado, fechaUso);
                    historial.add(historialDeUso);

                    // Imprimir cada multa encontrada
                    System.out.println("Id: " + id + " Id usuario: "+ idUsuario+ "id espacio: "+idEspacio);
                }
            }

            System.out.println("Total de multas encontradas: " + historial.size());

        } catch (SQLException e) {
            System.err.println("Error al mostrar historial de uso: " + e.getMessage());
            e.printStackTrace();
        }

        return historial;
    }


}
