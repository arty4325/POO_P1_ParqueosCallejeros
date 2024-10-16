package com.example.parqueoscallejeros.Reportes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class MultasDAO {

    private final String DB_URL = "jdbc:sqlite:databases/parqueoscallejeros.db";

    // Método para obtener todas las multas de la base de datos en un rango de tiempo
    public List<Multa> mostrarMultas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Multa> multas = new ArrayList<>();
        String sql = "SELECT * FROM Multas";

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
                int idInspector = rs.getInt("id_inspector");
                String placa = rs.getString("placa");
                double costo = rs.getDouble("costo");
                Timestamp fechaMultado = rs.getTimestamp("fecha_multado");
                System.out.println(placa + "\t" + costo + "\t" + fechaMultado);
                // Verificar si la fecha de la multa está dentro del rango
                if (fechaMultado.toLocalDateTime().isAfter(fechaInicio) && fechaMultado.toLocalDateTime().isBefore(fechaFin.plusNanos(1))) {
                    Multa multa = new Multa(id, idInspector, placa, costo, fechaMultado);
                    multas.add(multa);

                    // Imprimir cada multa encontrada
                    System.out.println("Multa encontrada: ID=" + id + ", Placa=" + placa + ", Costo=" + costo + ", Fecha=" + fechaMultado);
                }
            }

            System.out.println("Total de multas encontradas: " + multas.size());

        } catch (SQLException e) {
            System.err.println("Error al mostrar multas: " + e.getMessage());
            e.printStackTrace();
        }

        return multas;
    }


}
