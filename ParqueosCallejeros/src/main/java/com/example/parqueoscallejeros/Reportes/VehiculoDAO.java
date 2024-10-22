package com.example.parqueoscallejeros.Reportes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {
    private static final String DB_URL = "jdbc:sqlite:databases/parqueoscallejeros.db";

    /**
     * muestar los vehiculos con su info;
     * @return
     */
    public static List<Vehiculo> mostrarVehiculos() {
        List<Vehiculo> listVehiculo = new ArrayList<>();
        String sql = "SELECT * FROM Vehiculos";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) { // Usa la consulta corregida

            // Establecer las fechas en el PreparedStatement

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String placa = rs.getString("placa");
                String marca = rs.getString("marca");
                String modelo = rs.getString("modelo");
                int idUsuario = rs.getInt("id_usuario");
                int parqueado = rs.getInt("parqueado");
                Vehiculo vehiculo = new Vehiculo(id, placa, marca, modelo, idUsuario, parqueado);
                listVehiculo.add(vehiculo);
            }

        } catch (SQLException e) {
            System.err.println("Error al mostrar los vehiculos: " + e.getMessage());
            e.printStackTrace();
        }

        return listVehiculo;
    }
}