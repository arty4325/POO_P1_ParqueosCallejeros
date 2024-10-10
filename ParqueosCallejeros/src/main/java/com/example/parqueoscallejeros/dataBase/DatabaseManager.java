package com.example.parqueoscallejeros.dataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class DatabaseManager {
    // Ahora le doy la ruta a la base de datos
    private static final String DB_URL = "jdbc:sqlite:databases/parqueoscallejeros.db"; // Ruta relativa

    // Método para insertar un administrador
    public boolean insertarAdministrador(String nombre, String apellido, String telefono, String correo,
                                         String direccion, LocalDate fechaIngreso, String identificacionUsuario, String pin) {
        String sql = "INSERT INTO Administradores (nombre, apellidos, telefono, correo, direccion, fecha_ingreso, identificacion_usuario, pin) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, telefono);
            pstmt.setString(4, correo);
            pstmt.setString(5, direccion);
            pstmt.setString(6, fechaIngreso.toString()); // Asegúrate de que la fecha se formatee correctamente
            pstmt.setString(7, identificacionUsuario);
            pstmt.setString(8, pin);
            pstmt.executeUpdate(); // Ejecuta la inserción
            return true; // Inserción fue exitosa
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
            return false; // Si algo falla, la inserción no fue exitosa
        }
    }

    // Método para insertar un usuario
    public boolean insertarUsuario(String nombre, String apellido, String telefono, String correo,
                                   String direccion, String tarjetaCredito, String fechaVencimiento,
                                   String codigoValidacion, LocalDate fechaIngreso,
                                   String identificacion, String pin, int verificado) {
        String sql = "INSERT INTO Usuarios (nombre, apellidos, telefono, correo, direccion, tarjeta_credito, " +
                "fecha_vencimiento, codigo_validacion, fecha_ingreso, identificacion_usuario, pin, verificado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, telefono);
            pstmt.setString(4, correo);
            pstmt.setString(5, direccion);
            pstmt.setString(6, tarjetaCredito);
            pstmt.setString(7, fechaVencimiento); // Mes y año (ejemplo: "MM/YY")
            pstmt.setString(8, codigoValidacion);
            pstmt.setString(9, fechaIngreso.toString()); // Convertimos LocalDate a String
            pstmt.setString(10, identificacion);
            pstmt.setString(11, pin);
            pstmt.setInt(12, verificado);
            pstmt.executeUpdate(); // Ejecuta la inserción
            return true; // Inserción fue exitosa
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
            return false; // Si algo falla, la inserción no fue exitosa
        }
    }

    // Método para insertar un vehículo
    public boolean insertarVehiculo(int idUsuario, String placa, String marca, String modelo) {
        String sql = "INSERT INTO Vehiculos (id_usuario, placa, marca, modelo) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUsuario); // Vincula el vehículo con el ID del usuario
            pstmt.setString(2, placa);
            pstmt.setString(3, marca); // Puede ser null o vacío
            pstmt.setString(4, modelo); // Puede ser null o vacío
            pstmt.executeUpdate(); // Ejecuta la inserción
            return true; // Inserción fue exitosa
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
            return false; // Si algo falla, la inserción no fue exitosa
        }
    }

}
