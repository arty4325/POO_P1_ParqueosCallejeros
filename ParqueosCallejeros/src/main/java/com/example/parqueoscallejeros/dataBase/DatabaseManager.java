package com.example.parqueoscallejeros.dataBase;

import java.sql.*;
import java.time.LocalDate;

public class DatabaseManager {
    // Ahora le doy la ruta a la base de datos
    private static final String DB_URL = "jdbc:sqlite:databases/parqueoscallejeros.db"; // Ruta relativa

    // Método para insertar un administrador con codigo_validacion
    public boolean insertarAdministrador(String nombre, String apellido, String telefono, String correo,
                                         String direccion, LocalDate fechaIngreso, String identificacionUsuario,
                                         String pin, String codigoValidacion) {
        String sql = "INSERT INTO Administradores (nombre, apellidos, telefono, correo, direccion, fecha_ingreso, identificacion_usuario, pin, codigo_validacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, telefono);
            pstmt.setString(4, correo);
            pstmt.setString(5, direccion);
            pstmt.setString(6, fechaIngreso.toString()); // Convertimos LocalDate a String
            pstmt.setString(7, identificacionUsuario);
            pstmt.setString(8, pin);
            pstmt.setString(9, codigoValidacion); // Agregamos el código de validación
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

    public boolean verificarUsuario(String identificacionUsuario, String pin, String codigoValidacion) {
        String selectSql = "SELECT codigo_validacion FROM Usuarios WHERE identificacion_usuario = ? AND pin = ?";
        String updateSql = "UPDATE Usuarios SET verificado = 1 WHERE identificacion_usuario = ? AND pin = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
             PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {

            // Preparar la consulta select
            selectPstmt.setString(1, identificacionUsuario);
            selectPstmt.setString(2, pin);

            // Ejecutar la consulta select
            var rs = selectPstmt.executeQuery();

            // Verificar si el usuario fue encontrado y si el código de validación coincide
            if (rs.next()) {
                String codigoValidacionDb = rs.getString("codigo_validacion");
                if (codigoValidacionDb.equals(codigoValidacion)) {
                    // Si el código de validación coincide, actualizamos el campo "verificado"
                    updatePstmt.setString(1, identificacionUsuario);
                    updatePstmt.setString(2, pin);
                    updatePstmt.executeUpdate(); // Ejecuta la actualización
                    return true; // La verificación fue exitosa
                }
            }

            return false; // Si no se encontró el usuario o el código de validación no coincide
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si algo falla, se retorna false
        }
    }

    public boolean verificarAdministrador(String identificacionAdmin, String pin, String codigoValidacion) {
        String selectSql = "SELECT codigo_validacion FROM Administradores WHERE identificacion_usuario = ? AND pin = ?";
        String updateSql = "UPDATE Administradores SET verificado = 1 WHERE identificacion_usuario = ? AND pin = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement selectPstmt = conn.prepareStatement(selectSql);
             PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {

            // Preparar la consulta select
            selectPstmt.setString(1, identificacionAdmin);
            selectPstmt.setString(2, pin);

            // Ejecutar la consulta select
            var rs = selectPstmt.executeQuery();

            // Verificar si el administrador fue encontrado y si el código de validación coincide
            if (rs.next()) {
                String codigoValidacionDb = rs.getString("codigo_validacion");
                if (codigoValidacionDb.equals(codigoValidacion)) {
                    // Si el código de validación coincide, actualizamos el campo "verificado"
                    updatePstmt.setString(1, identificacionAdmin);
                    updatePstmt.setString(2, pin);
                    updatePstmt.executeUpdate(); // Ejecuta la actualización
                    return true; // La verificación fue exitosa
                }
            }

            return false; // Si no se encontró el administrador o el código de validación no coincide
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si algo falla, se retorna false
        }
    }


    public boolean iniciarSesion(String identificacionUsuario, String pin) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE identificacion_usuario = ? AND pin = ? AND verificado = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Preparar la consulta con los parámetros
            pstmt.setString(1, identificacionUsuario);
            pstmt.setString(2, pin);

            // Ejecutar la consulta
            var rs = pstmt.executeQuery();

            // Verificar si se encontró un usuario con esa combinación
            if (rs.next()) {
                int count = rs.getInt(1); // Obtiene el conteo de registros
                return count > 0; // Retorna true si hay al menos un resultado, de lo contrario false
            }

            return false; // No se encontró ninguna coincidencia
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si ocurre algún error, se retorna false
        }
    }

    public boolean iniciarSesionAdmin(String identificacionAdmin, String pin) {
        String sql = "SELECT COUNT(*) FROM Administradores WHERE identificacion_usuario = ? AND pin = ? AND verificado = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Preparar la consulta con los parámetros
            pstmt.setString(1, identificacionAdmin);
            pstmt.setString(2, pin);

            // Ejecutar la consulta
            var rs = pstmt.executeQuery();

            // Verificar si se encontró un administrador con esa combinación
            if (rs.next()) {
                int count = rs.getInt(1); // Obtiene el conteo de registros
                return count > 0; // Retorna true si hay al menos un resultado, de lo contrario false
            }

            return false; // No se encontró ninguna coincidencia
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si ocurre algún error, se retorna false
        }
    }


    public Integer obtenerIdUsuario(String identificacionUsuario, String pin) {
        String sql = "SELECT id FROM Usuarios WHERE identificacion_usuario = ? AND pin = ? AND verificado = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Preparar la consulta con los parámetros
            pstmt.setString(1, identificacionUsuario);
            pstmt.setString(2, pin);

            // Ejecutar la consulta
            var rs = pstmt.executeQuery();

            // Verificar si se encontró un usuario con esa combinación
            if (rs.next()) {
                return rs.getInt("id"); // Retorna el id del usuario encontrado
            }

            return null; // Retorna null si no se encontró ninguna coincidencia
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Si ocurre algún error, se retorna null
        }
    }

    public Integer obtenerIdAdmin(String identificacionAdmin, String pin) {
        String sql = "SELECT id FROM Administradores WHERE identificacion_usuario = ? AND pin = ? AND verificado = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Preparar la consulta con los parámetros
            pstmt.setString(1, identificacionAdmin);
            pstmt.setString(2, pin);

            // Ejecutar la consulta
            var rs = pstmt.executeQuery();

            // Verificar si se encontró un administrador con esa combinación
            if (rs.next()) {
                return rs.getInt("id"); // Retorna el id del administrador encontrado
            }

            return null; // Retorna null si no se encontró ninguna coincidencia
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Si ocurre algún error, se retorna null
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

    // Método para actualizar el codigo_cambio de un usuario
    public boolean actualizarCodigoCambioUsuario(String identificacionUsuario, int codigoCambio) {
        String sql = "UPDATE Usuarios SET codigo_cambio = ? WHERE identificacion_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigoCambio); // Asigna el nuevo codigo_cambio
            pstmt.setString(2, identificacionUsuario); // Asigna la identificacion_usuario
            int rowsAffected = pstmt.executeUpdate(); // Ejecuta la actualización
            return rowsAffected > 0; // Retorna true si se actualizó al menos un registro
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
            return false; // Si algo falla, la actualización no fue exitosa
        }
    }

    // Método para actualizar el codigo_cambio de un administrador
    public boolean actualizarCodigoCambioAdministrador(String identificacionAdmin, int codigoCambio) {
        String sql = "UPDATE Administradores SET codigo_cambio = ? WHERE identificacion_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigoCambio); // Asigna el nuevo codigo_cambio
            pstmt.setString(2, identificacionAdmin); // Asigna la identificacion_usuario
            int rowsAffected = pstmt.executeUpdate(); // Ejecuta la actualización
            return rowsAffected > 0; // Retorna true si se actualizó al menos un registro
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
            return false; // Si algo falla, la actualización no fue exitosa
        }
    }

    // Método para obtener el correo electrónico de un administrador por su identificacion_usuario
    public String obtenerCorreoAdministrador(String identificacionUsuario) {
        String sql = "SELECT correo FROM Administradores WHERE identificacion_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, identificacionUsuario);

            // Ejecutar la consulta
            ResultSet rs = pstmt.executeQuery();

            // Verificar si se encontró el administrador con esa identificación
            if (rs.next()) {
                return rs.getString("correo"); // Retorna el correo encontrado
            }

            return null; // Retorna null si no se encontró ninguna coincidencia
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Si ocurre algún error, se retorna null
        }
    }

    // Método para obtener el correo electrónico de un usuario por su identificacion_usuario
    public String obtenerCorreoUsuario(String identificacionUsuario) {
        String sql = "SELECT correo FROM Usuarios WHERE identificacion_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, identificacionUsuario);

            // Ejecutar la consulta
            ResultSet rs = pstmt.executeQuery();

            // Verificar si se encontró el usuario con esa identificación
            if (rs.next()) {
                return rs.getString("correo"); // Retorna el correo encontrado
            }

            return null; // Retorna null si no se encontró ninguna coincidencia
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Si ocurre algún error, se retorna null
        }
    }

    // Método para cambiar el PIN de un usuario
    public boolean cambiarPinUsuario(String identificacionUsuario, int codigoCambio, String nuevoPin) {
        // Verificar si la identificación y el código de cambio coinciden
        String verificarSql = "SELECT COUNT(*) FROM Usuarios WHERE identificacion_usuario = ? AND codigo_cambio = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmtVerificar = conn.prepareStatement(verificarSql)) {

            pstmtVerificar.setString(1, identificacionUsuario);
            pstmtVerificar.setInt(2, codigoCambio);

            // Ejecutar la consulta
            var rs = pstmtVerificar.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Si coincide, actualizar el PIN
                String updateSql = "UPDATE Usuarios SET pin = ? WHERE identificacion_usuario = ?";
                try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {
                    pstmtUpdate.setString(1, nuevoPin);
                    pstmtUpdate.setString(2, identificacionUsuario);
                    int rowsAffected = pstmtUpdate.executeUpdate(); // Ejecuta la actualización
                    return rowsAffected > 0; // Retorna true si se actualizó al menos un registro
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false si no se encontró coincidencia o si ocurrió un error
    }


    // Método para cambiar el PIN de un administrador
    public boolean cambiarPinAdministrador(String identificacionAdmin, int codigoCambio, String nuevoPin) {
        // Verificar si la identificación y el código de cambio coinciden
        String verificarSql = "SELECT COUNT(*) FROM Administradores WHERE identificacion_usuario = ? AND codigo_cambio = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmtVerificar = conn.prepareStatement(verificarSql)) {

            pstmtVerificar.setString(1, identificacionAdmin);
            pstmtVerificar.setInt(2, codigoCambio);

            // Ejecutar la consulta
            var rs = pstmtVerificar.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Si coincide, actualizar el PIN
                String updateSql = "UPDATE Administradores SET pin = ? WHERE identificacion_usuario = ?";
                try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {
                    pstmtUpdate.setString(1, nuevoPin);
                    pstmtUpdate.setString(2, identificacionAdmin);
                    int rowsAffected = pstmtUpdate.executeUpdate(); // Ejecuta la actualización
                    return rowsAffected > 0; // Retorna true si se actualizó al menos un registro
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false si no se encontró coincidencia o si ocurrió un error
    }





}
