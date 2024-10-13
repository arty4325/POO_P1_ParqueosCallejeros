package com.example.parqueoscallejeros.dataBase;
// cambio
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // Método para resetear el codigo_cambio de un usuario
    public boolean resetearCodigoCambioUsuario(String identificacionUsuario) {
        // Actualizar el codigo_cambio a 0
        String updateSql = "UPDATE Usuarios SET codigo_cambio = 0 WHERE identificacion_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {

            pstmtUpdate.setString(1, identificacionUsuario);
            int rowsAffected = pstmtUpdate.executeUpdate(); // Ejecuta la actualización
            return rowsAffected > 0; // Retorna true si se actualizó al menos un registro
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false si ocurrió un error
    }

    // Método para resetear el codigo_cambio de un administrador
    public boolean resetearCodigoCambioAdministrador(String identificacionAdmin) {
        // Actualizar el codigo_cambio a 0
        String updateSql = "UPDATE Administradores SET codigo_cambio = 0 WHERE identificacion_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {

            pstmtUpdate.setString(1, identificacionAdmin);
            int rowsAffected = pstmtUpdate.executeUpdate(); // Ejecuta la actualización
            return rowsAffected > 0; // Retorna true si se actualizó al menos un registro
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false si ocurrió un error
    }

    public boolean insertarConfiguracionParqueo(int id, String horarioInicio, String horarioFin, int precioPorHora,
                                                int tiempoMinimo, int costoMulta) {
        // Primero, verificamos si el ID ya existe
        if (verificarConfiguracionParqueoExistente(id)) {
            return false; // El ID ya existe, no se realiza la inserción
        }

        String sqlConfiguracion = "INSERT INTO ConfiguracionParqueo (id, horario_inicio, horario_fin, precio_por_hora, tiempo_minimo, costo_multa) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlEspacios = "INSERT INTO EspaciosParqueo (numero_espacio, estado) VALUES (?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            // Desactivar auto-commit para manejar ambas inserciones en una sola transacción
            conn.setAutoCommit(false);

            // Inserción en ConfiguracionParqueo
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlConfiguracion)) {
                pstmt1.setInt(1, id);
                pstmt1.setString(2, horarioInicio); // Inserta horario_inicio como String
                pstmt1.setString(3, horarioFin);    // Inserta horario_fin como String
                pstmt1.setInt(4, precioPorHora);
                pstmt1.setInt(5, tiempoMinimo);
                pstmt1.setInt(6, costoMulta);
                pstmt1.executeUpdate(); // Ejecuta la inserción en ConfiguracionParqueo
            }

            // Inserción en EspaciosParqueos con cantidad_espacios como 0
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlEspacios)) {
                pstmt2.setInt(1, id);
                pstmt2.setInt(2, 0); // Inserta cantidad_espacios como 0
                pstmt2.executeUpdate(); // Ejecuta la inserción en EspaciosParqueos
            }

            // Si todo sale bien, confirma la transacción
            conn.commit();
            return true; // Inserciones fueron exitosas
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
            return false; // Si algo falla, la eliminación no fue exitosa
        }
    }


    public boolean eliminarConfiguracionParqueo(int id) {
        // Verificamos si el ID existe
        if (!verificarConfiguracionParqueoExistente(id)) {
            return false; // El ID no existe, no se realiza la eliminación
        }

        // Primero eliminamos los registros relacionados en EspaciosParqueo
        String deleteEspaciosSql = "DELETE FROM EspaciosParqueo WHERE numero_espacio = ?";

        // Luego eliminamos el registro de ConfiguracionParqueo
        String deleteConfiguracionSql = "DELETE FROM ConfiguracionParqueo WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement deleteEspaciosStmt = conn.prepareStatement(deleteEspaciosSql);
             PreparedStatement deleteConfiguracionStmt = conn.prepareStatement(deleteConfiguracionSql)) {

            // Para eliminar en EspaciosParqueo
            deleteEspaciosStmt.setInt(1, id);
            deleteEspaciosStmt.executeUpdate(); // Ejecutamos la eliminación en EspaciosParqueo

            // Para eliminar en ConfiguracionParqueo
            deleteConfiguracionStmt.setInt(1, id);
            int rowsAffected = deleteConfiguracionStmt.executeUpdate(); // Ejecutamos la eliminación en ConfiguracionParqueo

            return rowsAffected > 0; // Devuelve true si se eliminó al menos una fila en ConfiguracionParqueo

        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
            return false; // Si algo falla, la eliminación no fue exitosa
        }
    }



    public boolean verificarConfiguracionParqueoExistente(int id) {
        String sql = "SELECT COUNT(*) FROM ConfiguracionParqueo WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);

            // Ejecutar la consulta
            var rs = pstmt.executeQuery();

            // Verificar si se encontró un registro con ese ID
            if (rs.next()) {
                int count = rs.getInt(1); // Obtiene el conteo de registros
                return count > 0; // Retorna true si hay al menos un registro, de lo contrario false
            }

            return false; // No se encontró ningún registro
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si ocurre algún error, se retorna false
        }
    }

    public List<String> obtenerPlacasPorUsuario(int idUsuario) {
        List<String> placas = new ArrayList<>();
        // Consulta modificada para verificar que parqueado sea 0
        String sql = "SELECT placa FROM Vehiculos WHERE id_usuario = ? AND parqueado = 0";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            // Ejecutar la consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String placa = rs.getString("placa"); // Obtener la placa
                    placas.add(placa); // Agregar a la lista
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
        }

        return placas; // Retorna la lista de placas
    }

    public List<String> obtenerPlacasOcupadasPorUsuario(int idUsuario) {
        List<String> placas = new ArrayList<>();
        // Consulta para verificar que parqueado sea 1 (ocupado)
        String sql = "SELECT placa FROM Vehiculos WHERE id_usuario = ? AND parqueado = 1";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idUsuario);

            // Ejecutar la consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String placa = rs.getString("placa"); // Obtener la placa
                    placas.add(placa); // Agregar a la lista
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
        }

        return placas; // Retorna la lista de placas ocupadas
    }



    public List<Integer> obtenerEspaciosDisponibles() {
        List<Integer> espaciosDisponibles = new ArrayList<>();
        String sql = "SELECT numero_espacio FROM EspaciosParqueo WHERE estado = 0";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int numeroEspacio = rs.getInt("numero_espacio"); // Obtener el número de espacio
                espaciosDisponibles.add(numeroEspacio); // Agregar a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
        }

        return espaciosDisponibles; // Retorna la lista de espacios disponibles
    }

    public boolean estaEspacioDisponible(int numeroEspacio) {
        String sql = "SELECT estado FROM EspaciosParqueo WHERE numero_espacio = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, numeroEspacio);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int estado = rs.getInt("estado"); // Obtener el estado del espacio
                    return estado == 0; // Retorna true si está en estado 0, false si está en 1
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
        }

        return false; // Retorna false si no se encuentra el espacio o si tiene estado 1
    }

    public String obtenerHorarioInicio(int idConfiguracion) {
        String sql = "SELECT horario_inicio FROM ConfiguracionParqueo WHERE id = ?";
        String horarioInicio = null;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idConfiguracion);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    horarioInicio = rs.getString("horario_inicio"); // Obtener el horario de inicio como String
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
        }

        return horarioInicio; // Retorna el horario de inicio como String
    }

    public String obtenerHorarioFin(int idConfiguracion) {
        String sql = "SELECT horario_fin FROM ConfiguracionParqueo WHERE id = ?";
        String horarioFin = null;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idConfiguracion);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    horarioFin = rs.getString("horario_fin"); // Obtener el horario de fin como String
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
        }

        return horarioFin; // Retorna el horario de fin como String
    }



    public int obtenerPrecioPorHora(int idConfiguracion) {
        String sql = "SELECT precio_por_hora FROM ConfiguracionParqueo WHERE id = ?";
        int precioPorHora = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idConfiguracion);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    precioPorHora = rs.getInt("precio_por_hora"); // Obtener el precio por hora
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
        }

        return precioPorHora; // Retorna el precio por hora
    }



    public int obtenerTiempoMinimo(int idConfiguracion) {
        String sql = "SELECT tiempo_minimo FROM ConfiguracionParqueo WHERE id = ?";
        int tiempoMinimo = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idConfiguracion);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tiempoMinimo = rs.getInt("tiempo_minimo"); // Obtener el tiempo mínimo
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
        }

        return tiempoMinimo; // Retorna el tiempo mínimo
    }


    public int obtenerCostoMulta(int idConfiguracion) {
        String sql = "SELECT costo_multa FROM ConfiguracionParqueo WHERE id = ?";
        int costoMulta = 0;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idConfiguracion);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    costoMulta = rs.getInt("costo_multa"); // Obtener el costo de multa
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
        }

        return costoMulta; // Retorna el costo de multa
    }

    public void insertarReserva(int idUsuario, int idEspacio, String placa, int tiempoReservado, int costo, String fechaReserva) {
        String sql = "INSERT INTO Reservas (id_usuario, id_espacio, placa, tiempo_reservado, costo, fecha_reserva) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar los valores a cada parámetro de la consulta
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idEspacio);
            pstmt.setString(3, placa);
            pstmt.setInt(4, tiempoReservado);
            pstmt.setInt(5, costo);
            pstmt.setString(6, fechaReserva); // Asignar fecha como String

            // Ejecutar la consulta
            pstmt.executeUpdate();
            System.out.println("Reserva insertada correctamente.");
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }

    public void insertarHistorialUso(int idUsuario, int idEspacio, int costo, int tiempoOcupado, String fechaUso) {
        String sql = "INSERT INTO HistorialUso (id_usuario, id_espacio, costo, tiempo_ocupado, fecha_uso) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar los valores a cada parámetro de la consulta
            pstmt.setInt(1, idUsuario);
            pstmt.setInt(2, idEspacio);
            pstmt.setInt(3, costo);
            pstmt.setInt(4, tiempoOcupado);
            pstmt.setString(5, fechaUso); // Asignar fecha como String

            // Ejecutar la consulta
            pstmt.executeUpdate();
            System.out.println("Historial de uso insertado correctamente.");
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }


    public void actualizarEstadoEspacioParqueado(int numeroEspacio) {
        String sql = "UPDATE EspaciosParqueo SET estado = 1 WHERE numero_espacio = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar el valor del número de espacio al parámetro de la consulta
            pstmt.setInt(1, numeroEspacio);

            // Ejecutar la actualización
            int rowsAffected = pstmt.executeUpdate();

            // Verificar si la actualización fue exitosa
            if (rowsAffected > 0) {
                System.out.println("El estado del espacio ha sido actualizado correctamente.");
            } else {
                System.out.println("No se encontró ningún espacio con ese número.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }

    public void actualizarEstadoVehiculoParqueado(int idUsuario, String placa) {
        String sql = "UPDATE Vehiculos SET parqueado = 1 WHERE placa = ? AND id_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar los valores de placa y idUsuario a los parámetros de la consulta
            pstmt.setString(1, placa);
            pstmt.setInt(2, idUsuario);

            // Ejecutar la actualización
            int rowsAffected = pstmt.executeUpdate();

            // Verificar si la actualización fue exitosa
            if (rowsAffected > 0) {
                System.out.println("El estado del vehículo ha sido actualizado correctamente.");
            } else {
                System.out.println("No se encontró ningún vehículo con esa placa y usuario.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }

    public Integer obtenerIdEspacioPorPlaca(String placa) {
        Integer idEspacio = null; // Inicializar la variable a null para indicar que no se ha encontrado

        // Consulta para obtener el id_espacio basado en la placa
        String sql = "SELECT id_espacio FROM Reservas WHERE placa = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, placa); // Asignar la placa al parámetro de la consulta

            // Ejecutar la consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idEspacio = rs.getInt("id_espacio"); // Obtener el id_espacio
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }

        return idEspacio; // Retornar el id_espacio o null si no se encontró
    }

    public String obtenerFechaReserva(int idReserva) {
        String sql = "SELECT fecha_reserva FROM Reservas WHERE id_espacio = ?"; // Asegúrate de que la columna se llama correctamente
        String fechaReserva = null;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idReserva); // Establecer el id de reserva como parámetro

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    fechaReserva = rs.getString("fecha_reserva"); // Obtener la fecha de reserva como String
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }

        return fechaReserva; // Retornar la fecha de reserva como String
    }

    public Integer obtenerTiempoReservado(int idReserva) {
        String sql = "SELECT tiempo_reservado FROM Reservas WHERE id_espacio = ?"; // Asegúrate de que la columna se llama correctamente
        Integer tiempoReservado = null; // Usamos Integer para permitir que pueda ser nulo

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idReserva); // Establecer el id de reserva como parámetro

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    tiempoReservado = rs.getInt("tiempo_reservado"); // Obtener el tiempo reservado como Integer
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }

        return tiempoReservado; // Retornar el tiempo reservado como Integer
    }

    public void actualizarEstadoVehiculoDesparqueado(String placa) {
        String sql = "UPDATE Vehiculos SET parqueado = 0 WHERE placa = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar el valor de la placa al parámetro de la consulta
            pstmt.setString(1, placa);

            // Ejecutar la actualización
            int rowsAffected = pstmt.executeUpdate();

            // Verificar si la actualización fue exitosa
            if (rowsAffected > 0) {
                System.out.println("El estado del vehículo ha sido actualizado a no parqueado correctamente.");
            } else {
                System.out.println("No se encontró ningún vehículo con esa placa.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }

    public void actualizarEstadoEspacioDesparqueado(int numeroEspacio) {
        String sql = "UPDATE EspaciosParqueo SET estado = 0 WHERE numero_espacio = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar el valor del número de espacio al parámetro de la consulta
            pstmt.setInt(1, numeroEspacio);

            // Ejecutar la actualización
            int rowsAffected = pstmt.executeUpdate();

            // Verificar si la actualización fue exitosa
            if (rowsAffected > 0) {
                System.out.println("El estado del espacio ha sido actualizado a no ocupado correctamente.");
            } else {
                System.out.println("No se encontró ningún espacio con ese número.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }
    public void actualizarTiempoOcupado(int idUsuario, int nuevoTiempoOcupado) {
        String sql = "UPDATE HistorialUso SET tiempo_ocupado = ? WHERE id_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar los valores a cada parámetro de la consulta
            pstmt.setInt(1, nuevoTiempoOcupado);
            pstmt.setInt(2, idUsuario);

            // Ejecutar la actualización
            int rowsAffected = pstmt.executeUpdate();

            // Verificar si la actualización fue exitosa
            if (rowsAffected > 0) {
                System.out.println("El tiempo ocupado ha sido actualizado correctamente.");
            } else {
                System.out.println("No se encontró ningún historial de uso con ese usuario.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }
    public void eliminarReservaPorEspacio(int idEspacio) {
        String sql = "DELETE FROM Reservas WHERE id_espacio = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar el valor de idEspacio al parámetro de la consulta
            pstmt.setInt(1, idEspacio);

            // Ejecutar la eliminación
            int rowsAffected = pstmt.executeUpdate();

            // Verificar si la eliminación fue exitosa
            if (rowsAffected > 0) {
                System.out.println("La reserva ha sido eliminada correctamente.");
            } else {
                System.out.println("No se encontró ninguna reserva con ese id de espacio.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }

    public void sumarAcumuladosPorUsuario(int idUsuario, int nuevosAcumulados) {
        String sql = "UPDATE Usuarios SET acumulados = acumulados + ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar el nuevo valor de acumulados y el idUsuario a los parámetros de la consulta
            pstmt.setInt(1, nuevosAcumulados);
            pstmt.setInt(2, idUsuario);

            // Ejecutar la actualización
            int rowsAffected = pstmt.executeUpdate();

            // Verificar si la actualización fue exitosa
            if (rowsAffected > 0) {
                System.out.println("Los acumulados del usuario han sido actualizados correctamente.");
            } else {
                System.out.println("No se encontró ningún usuario con ese id.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }


















}
