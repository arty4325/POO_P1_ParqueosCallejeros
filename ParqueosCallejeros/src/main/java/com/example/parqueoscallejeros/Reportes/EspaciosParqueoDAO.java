package com.example.parqueoscallejeros.Reportes;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EspaciosParqueoDAO {
    private final String DB_URL = "jdbc:sqlite:databases/parqueoscallejeros.db";

    // Método para obtener todas las multas de la base de datos en un rango de tiempo
    public List<EspaciosParqueo> mostrarEspaciosParqueo(int espacioMostrar) {
        List<EspaciosParqueo> listEspaciosParqueo = new ArrayList<>();
        String sql = "SELECT * FROM EspaciosParqueo";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Usa la consulta corregida

            // Establecer las fechas en el PreparedStatement

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                int idEspacio = rs.getInt("numero_espacio");
                int estado = rs.getInt("estado");
                if (espacioMostrar == estado || espacioMostrar == 2) {
                    EspaciosParqueo espacio = new EspaciosParqueo(id, idEspacio, estado);
                    listEspaciosParqueo.add(espacio);

                    // Imprimir cada multa encontrada
                    System.out.println("Id: " + id + " Id usuario: "+ "id espacio: "+idEspacio + " Estado: " + estado);
                }
            }

            System.out.println("Total de reservas encontradas: " + listEspaciosParqueo.size());

        } catch (SQLException e) {
            System.err.println("Error al mostrar las reservas: " + e.getMessage());
            e.printStackTrace();
        }

        return listEspaciosParqueo;
    }
}