package com.example.parqueoscallejeros.dataBase;
// cambio
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    // Se le da al programa la direccion de la base de datos
    private static final String DB_URL = "jdbc:sqlite:databases/parqueoscallejeros.db"; // Ruta relativa de la base de datos

    // Método para insertar un administrador con codigo_validacion

    /**
     * Funcion que permite incertar un administrador
     * @param nombre nombre del administrador
     * @param apellido apellido del administrador
     * @param telefono telefono del administrador
     * @param correo correo del administrador
     * @param direccion direccion del administrador
     * @param fechaIngreso fecha de ingreso del administrador
     * @param identificacionUsuario identificacion del administrador
     * @param pin pin del administrador
     * @param codigoValidacion codigo de validacion del administrador
     * @return
     */
    public boolean insertarAdministrador(String nombre, String apellido, String telefono, String correo,
                                         String direccion, LocalDate fechaIngreso, String identificacionUsuario,
                                         String pin, String codigoValidacion) {
        // Incerta en sql toda la informacion de un administrador
        String sql = "INSERT INTO Administradores (nombre, apellidos, telefono, correo, direccion, fecha_ingreso, identificacion_usuario, pin, codigo_validacion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Se hace la coneccion con la base de datos y se envia la informacion
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
            pstmt.setString(9, codigoValidacion); // Se manda de una ves el codigo de validacion
            pstmt.executeUpdate(); // Ejecuta la inserción
            return true; // Inserción fue exitosa
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
            return false; // Si algo falla, la inserción no fue exitosa
        }
    }


    /**
     * Funcion que permite incertar un usuario
     * @param nombre nombre del usuario
     * @param apellido apellido del usuario
     * @param telefono telefono dle usuaio
     * @param correo correo del usuario
     * @param direccion direccion del usuario
     * @param tarjetaCredito tarjeta del usuario
     * @param fechaVencimiento fecha de vencimiento de la tarjeta
     * @param codigoValidacion codigo de validacion
     * @param fechaIngreso fecha de ingreso del usuario
     * @param identificacion identificacion del usuario
     * @param pin pin del usuario
     * @param verificado verificacion del usuario
     * @return
     */
    public boolean insertarUsuario(String nombre, String apellido, String telefono, String correo,
                                   String direccion, String tarjetaCredito, String fechaVencimiento,
                                   String codigoValidacion, LocalDate fechaIngreso,
                                   String identificacion, String pin, int verificado) {
        /**
         * Funcion que permite incertar usuario en sql
         */
        String sql = "INSERT INTO Usuarios (nombre, apellidos, telefono, correo, direccion, tarjeta_credito, " +
                "fecha_vencimiento, codigo_validacion, fecha_ingreso, identificacion_usuario, pin, verificado) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        /**
         * Se envia la solicitud a la base de datos con la informacion que se quiere incertar
         */
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, telefono);
            pstmt.setString(4, correo);
            pstmt.setString(5, direccion);
            pstmt.setString(6, tarjetaCredito);
            pstmt.setString(7, fechaVencimiento);
            pstmt.setString(8, codigoValidacion);
            pstmt.setString(9, fechaIngreso.toString());
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

    /**
     * Funcion que permite incertar por primera ves un inspector
     * @param nombre El nombre del inspector
     * @param apellido el apellido del inspector
     * @param telefono el telefono del inspector
     * @param correo el correo del inspector
     * @param direccion la direccion del inspector
     * @param fechaIngreso la fecha de ingreso del inspector
     * @param identificacionUsuario la identificacion del inspector
     * @param pin el pin del inspector
     * @param terminalInspeccion la terminal de inspeccion del inspector
     * @return
     */
    public boolean insertarInspector(String nombre, String apellido, String telefono, String correo,
                                     String direccion, LocalDate fechaIngreso, String identificacionUsuario,
                                     String pin, String terminalInspeccion) {
        String sql = "INSERT INTO Inspectores (nombre, apellidos, telefono, correo, direccion, fecha_ingreso, identificacion_usuario, pin, terminal_inspeccion) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        // Comando que permite incertar en sql

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, apellido);
            pstmt.setString(3, telefono);
            pstmt.setString(4, correo);
            pstmt.setString(5, direccion);
            pstmt.setString(6, fechaIngreso.toString());
            pstmt.setString(7, identificacionUsuario);
            pstmt.setString(8, pin);
            pstmt.setString(9, terminalInspeccion);
            pstmt.executeUpdate(); // Ejecuta la inserción
            return true; // Inserción fue exitosa
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
            return false; // Si algo falla, la inserción no fue exitosa
        }
    }

    /**
     * Funcion que permite obtener el correo que esta asociado a una placa en la base de datos
     * @param placa
     * @return
     */
    public String obtenerCorreoPorPlaca(String placa) {
        String correo = null; // Inicializar correo como null
        String sql = "SELECT U.correo FROM Vehiculos V LEFT JOIN Usuarios U ON V.id_usuario = U.id WHERE V.placa = ?";
        // Se selecciona el correo, a partir de la placa del vehiculo

        try (Connection conn = DriverManager.getConnection(DB_URL); // Se establece una coneccion con la base de datos
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, placa); // Establecer la placa en la consulta

            // Ejecutar la consulta
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    correo = rs.getString("correo"); // Obtener el correo si existe
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }

        return correo; // Retornar el correo o null si no se encontró
    }


    /**
     * Funcion que permite verificar un usuario
     * @param identificacionUsuario El id del usuario
     * @param pin el pin
     * @param codigoValidacion el codigo de validacion enviado al correo
     * @return true si fue validado, false si no
     */
    public boolean verificarUsuario(String identificacionUsuario, String pin, String codigoValidacion) {
        String selectSql = "SELECT codigo_validacion FROM Usuarios WHERE identificacion_usuario = ? AND pin = ?";
        String updateSql = "UPDATE Usuarios SET verificado = 1 WHERE identificacion_usuario = ? AND pin = ?";
        // Se hacen dos concultas, se selecciona el codigo de validacion, para compararlo y de esta manera hacer un update a la base de datos si
        // la verificacion es valida

        try (Connection conn = DriverManager.getConnection(DB_URL); // Se establece una coneccion con la base de datos
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

    /**
     * Funcion que permite verificar a los administradores
     * @param identificacionAdmin Se envia la identificccion del admin
     * @param pin se envia el pin del admin
     * @param codigoValidacion se envia el codigo de validacion del admin
     * @return
     */
    public boolean verificarAdministrador(String identificacionAdmin, String pin, String codigoValidacion) {
        String selectSql = "SELECT codigo_validacion FROM Administradores WHERE identificacion_usuario = ? AND pin = ?";
        String updateSql = "UPDATE Administradores SET verificado = 1 WHERE identificacion_usuario = ? AND pin = ?";
        // LLamadas analogas al punto anterior

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

    /**
     * Funcion que permite iniciar sesion siendo usuario
     * @param identificacionUsuario el id del usuario
     * @param pin su pin
     * @return true si se logra iniciar sesion, false si no
     */
    public boolean iniciarSesion(String identificacionUsuario, String pin) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE identificacion_usuario = ? AND pin = ? AND verificado = 1";
        // Se envian los datos para contar las coincidencias

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

    /**
     * funcion que permite iniciar sesion siendo admin
     * @param identificacionAdmin la id del admin
     * @param pin el pin del admin
     * @return true si los credenciales son correctos, false con lo contrario
     */
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

    /**
     * funcion que permite iniciar sesion sinedo un inspector
     * @param identificacionInspector el id del inspector
     * @param pin el pin del inspector
     * @return true si los credenciales son correctos, false en lo contrario
     */
    public boolean iniciarSesionInspector(String identificacionInspector, String pin) {
        String sql = "SELECT COUNT(*) FROM Inspectores WHERE identificacion_usuario = ? AND pin = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Preparar la consulta con los parámetros
            pstmt.setString(1, identificacionInspector);
            pstmt.setString(2, pin);

            // Ejecutar la consulta
            var rs = pstmt.executeQuery();

            // Verificar si se encontró un inspector con esa combinación
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


    /**
     * para una identificacion de usuario y de pin me devuelve el id
     * @param identificacionUsuario el id del usuario
     * @param pin el pin de ese usuario
     * @return el id de ese usuario (El numerico)
     */
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

    /**
     * Funcion que permite obtener el id del admin
     * @param identificacionAdmin
     * @param pin
     * @return
     */
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

    /**
     * funcion que permite obtener el id del inspector
     * @param identificacionInspector
     * @param pin
     * @return
     */
    public Integer obtenerIdInspector(String identificacionInspector, String pin) {
        String sql = "SELECT id FROM Inspectores WHERE identificacion_usuario = ? AND pin = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Preparar la consulta con los parámetros
            pstmt.setString(1, identificacionInspector);
            pstmt.setString(2, pin);

            // Ejecutar la consulta
            var rs = pstmt.executeQuery();

            // Verificar si se encontró un inspector con esa combinación
            if (rs.next()) {
                return rs.getInt("id"); // Retorna el id del inspector encontrado
            }

            return null; // Retorna null si no se encontró ninguna coincidencia
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Si ocurre algún error, se retorna null
        }
    }


    /**
     * Funcion que perite incertar un vehiculo en la base de datos
     * @param idUsuario el id del usuario
     * @param placa la placa del carr
     * @param marca la marca del carro
     * @param modelo el modelo del carro
     * @return true si se logro meter la informacion dne la db
     */
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
            e.printStackTrace();
            return false; // Si algo falla, la inserción no fue exitosa
        }
    }

    /**
     * Funcion que verifica la existencia de un inspector
     * @param identificacionInspector la identificacion de ese inspector
     * @return true en el caso de que si existe, false en caso contrario
     */
    public boolean existeInspector(String identificacionInspector) {
        String sql = "SELECT COUNT(*) FROM Inspectores WHERE identificacion_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, identificacionInspector); // Asignar el valor de la identificación

            // Ejecutar la consulta
            ResultSet rs = pstmt.executeQuery();

            // Verificar si se encontró algún inspector con esa identificación
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true si el conteo es mayor que 0, lo que significa que existe
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejo de errores
        }
        return false; // Si ocurre un error o no se encontró coincidencia
    }

    /**
     * Funcion que permite cambiar el codigo de cambio de un usuario
     * @param identificacionUsuario el id de ese usuario
     * @param codigoCambio el codigo de cambio de ese usuario
     * @return devuelve true en el caso de que lo logre hacer satisfactoriamente
     */
    public boolean actualizarCodigoCambioUsuario(String identificacionUsuario, int codigoCambio) {
        String sql = "UPDATE Usuarios SET codigo_cambio = ? WHERE identificacion_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigoCambio); // Asigna el nuevo codigo_cambio
            pstmt.setString(2, identificacionUsuario); // Asigna la identificacion_usuario
            int rowsAffected = pstmt.executeUpdate(); // Ejecuta la actualización
            return rowsAffected > 0; // Retorna true si se actualizó al menos un registro
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si algo falla, la actualización no fue exitosa
        }
    }

    /**
     * Permite actualizar el codigo de cambio de un administrador
     * @param identificacionAdmin
     * @param codigoCambio
     * @return
     */
    public boolean actualizarCodigoCambioAdministrador(String identificacionAdmin, int codigoCambio) {
        String sql = "UPDATE Administradores SET codigo_cambio = ? WHERE identificacion_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigoCambio); // Asigna el nuevo codigo_cambio
            pstmt.setString(2, identificacionAdmin); // Asigna la identificacion_usuario
            int rowsAffected = pstmt.executeUpdate(); // Ejecuta la actualización
            return rowsAffected > 0; // Retorna true si se actualizó al menos un registro
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si algo falla, la actualización no fue exitosa
        }
    }

    /**
     * permite actualizar el codigo de cambio de un inspector
     * @param identificacionInspector
     * @param codigoCambio
     * @return
     */
    public boolean actualizarCodigoCambioInspector(String identificacionInspector, int codigoCambio) {
        String sql = "UPDATE Inspectores SET codigo_cambio = ? WHERE identificacion_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codigoCambio); // Asigna el nuevo codigo_cambio
            pstmt.setString(2, identificacionInspector); // Asigna la identificacion_usuario del inspector
            int rowsAffected = pstmt.executeUpdate(); // Ejecuta la actualización
            return rowsAffected > 0; // Retorna true si se actualizó al menos un registro
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si algo falla, la actualización no fue exitosa
        }
    }


    /**
     * Funcion que obtiene el correo de un administrador si es necesario
     * @param identificacionUsuario
     * @return
     */
    public String obtenerCorreoAdministrador(String identificacionUsuario) {
        String sql = "SELECT correo FROM Administradores WHERE identificacion_usuario = ?";
        // Se hace la consulta de sql para el id de ese usuario
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

    /**
     * Funcion que obitiene el correo de un usuario con el id de este
     * @param identificacionUsuario
     * @return
     */
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

    /**
     * Funcion que obtiene el correo de un inspector dandole un id
     * @param identificacionInspector
     * @return
     */
    public String obtenerCorreoInspector(String identificacionInspector) {
        String sql = "SELECT correo FROM Inspectores WHERE identificacion_usuario = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, identificacionInspector);

            // Ejecutar la consulta
            ResultSet rs = pstmt.executeQuery();

            // Verificar si se encontró el inspector con esa identificación
            if (rs.next()) {
                return rs.getString("correo"); // Retorna el correo encontrado
            }

            return null; // Retorna null si no se encontró ninguna coincidencia
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Si ocurre algún error, se retorna null
        }
    }


    /**
     * Funcion que permite cambiar el id de un usuario
     * @param identificacionUsuario el id del usuario al que le quiero cambiar la contra
     * @param codigoCambio el codigo de cambio que se envio al correo
     * @param nuevoPin el nuevo pin
     * @return true si logra hacer el cambio, false en caso contrario
     */
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

    /**
     * Funcion que permite cambiar el pin de un administrador si es necesario
     * @param identificacionAdmin
     * @param codigoCambio
     * @param nuevoPin
     * @return
     */
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

    /**
     * Funcion que permite cambiar el pin de un inspector si esto es necesario
     * @param identificacionInspector
     * @param codigoCambio
     * @param nuevoPin
     * @return
     */
    public boolean cambiarPinInspector(String identificacionInspector, int codigoCambio, String nuevoPin) {
        // Verificar si la identificación y el código de cambio coinciden
        String verificarSql = "SELECT COUNT(*) FROM Inspectores WHERE identificacion_usuario = ? AND codigo_cambio = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmtVerificar = conn.prepareStatement(verificarSql)) {

            pstmtVerificar.setString(1, identificacionInspector);
            pstmtVerificar.setInt(2, codigoCambio);

            // Ejecutar la consulta
            var rs = pstmtVerificar.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                // Si coincide, actualizar el PIN
                String updateSql = "UPDATE Inspectores SET pin = ? WHERE identificacion_usuario = ?";
                try (PreparedStatement pstmtUpdate = conn.prepareStatement(updateSql)) {
                    pstmtUpdate.setString(1, nuevoPin);
                    pstmtUpdate.setString(2, identificacionInspector);
                    int rowsAffected = pstmtUpdate.executeUpdate(); // Ejecuta la actualización
                    return rowsAffected > 0; // Retorna true si se actualizó al menos un registro
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Retorna false si no se encontró coincidencia o si ocurrió un error
    }


    /**
     * Este metodo permite tomar un usuario y devolver el codigo de cambio a su valor de 0
     * @param identificacionUsuario
     * @return
     */
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

    /**
     * Este metodo permite tomar un administrador y devolver el valor del codigo de cambio a lo original en 0
     * @param identificacionAdmin
     * @return
     */
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

    /**
     * Este codigo permite incertar la configuracion de un parqueo
     * @param id el id del parqueo
     * @param horarioInicio el horario de inicio de operacion
     * @param horarioFin el horario de fin de operacion
     * @param precioPorHora el precio por hora
     * @param tiempoMinimo el tiempo minimo de operacion
     * @param costoMulta el costo de multa
     * @return devuelve true en el caso de que se incerte la configuracion bien
     */
    public boolean insertarConfiguracionParqueo(int id, String horarioInicio, String horarioFin, int precioPorHora,
                                                int tiempoMinimo, int costoMulta) {
        // Primero, verificamos si el ID ya existe
        if (verificarConfiguracionParqueoExistente(id)) {
            return false; // El ID ya existe, no se realiza la inserción
        }

        String sqlConfiguracion = "INSERT INTO ConfiguracionParqueo (id, horario_inicio, horario_fin, precio_por_hora, tiempo_minimo, costo_multa) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        String sqlEspacios = "INSERT INTO EspaciosParqueo (numero_espacio, estado) VALUES (?, ?)";
        // Se tiene que incertar en configuracion y en espacios

        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            conn.setAutoCommit(false); // Esto se desactiva para poder mandar las dos interacciones

            // Inserción en ConfiguracionParqueo
            try (PreparedStatement pstmt1 = conn.prepareStatement(sqlConfiguracion)) {
                pstmt1.setInt(1, id);
                pstmt1.setString(2, horarioInicio);
                pstmt1.setString(3, horarioFin);
                pstmt1.setInt(4, precioPorHora);
                pstmt1.setInt(5, tiempoMinimo);
                pstmt1.setInt(6, costoMulta);
                pstmt1.executeUpdate();
            }

            // Inserción en EspaciosParqueos con cantidad_espacios como 0
            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlEspacios)) {
                pstmt2.setInt(1, id);
                pstmt2.setInt(2, 0); // Inserta cantidad_espacios como 0
                pstmt2.executeUpdate(); // Ejecuta la inserción en EspaciosParqueos
            }

            conn.commit();
            return true; // Inserciones fueron exitosas
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Si algo falla, la eliminación no fue exitosa
        }
    }


    /**
     * Esta funcion recibe el id del parqueo y lo elimina
     * @param id id del parqueo
     * @return true si logro eliminar elparqueo
     */
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
            e.printStackTrace();
            return false; // Si algo falla, la eliminación no fue exitosa
        }
    }


    /**
     * Verificacion de la configuracion de un paruqeo que ya existe
     * @param id el id del parqueo que ya es existente
     * @return true si el parqueo es existente
     */
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

    /**
     * Obtiene las placas de los carros que tiene un usuario
     * @param idUsuario
     * @return
     */
    public List<String> obtenerPlacasPorUsuario(int idUsuario) {
        List<String> placas = new ArrayList<>();
        // se quiere que no este parqueado por que son las opciones que se le van a dar al usuario para parquear
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
            e.printStackTrace();
        }

        return placas; // Retorna la lista de placas
    }

    /**
     * Funcion que permite obtener las placas que estan ocupadas por un usuario
     * @param idUsuario el id del usuario que se quiere revisar
     * @return una lista con las placas
     */
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
            e.printStackTrace();
        }

        return placas; // Retorna la lista de placas ocupadas
    }


    /**
     * Funcion que permite obtener los espacios disponibles
     * @return Una lista con los espacios disponibles
     */
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

    /**
     * Funcion que verifica para un espacio en concreto si este esta disponible
     * @param numeroEspacio Se le da el numero de espacio
     * @return se retorna true si esta disponible, false en caso contrario
     */
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
            e.printStackTrace();
        }

        return false; // Retorna false si no se encuentra el espacio o si tiene estado 1
    }

    /**
     * Funcion que permite obtener el horario de inicio
     * @param idConfiguracion
     * @return
     */
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
            e.printStackTrace();
        }

        return horarioInicio; // Retorna el horario de inicio como String
    }

    /**
     * Funcion que permite obtener el horario de fin de un parqueo
     * @param idConfiguracion
     * @return
     */
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


    /**
     * Funcion que permite obtener el precio por hora de un parqueo dado el id de la configuracion de este
     * @param idConfiguracion
     * @return el precio por hroa del parqueo
     */
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
            e.printStackTrace();
        }

        return precioPorHora; // Retorna el precio por hora
    }


    /**
     * Funcion que permite obtener el tiempo minimo que se tiene que estar parqueado
     * @param idConfiguracion El id de la configuracion de ese parqueo
     * @return el tiempo minimo que tiene que estar parqueado
     */
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
            e.printStackTrace();
        }

        return tiempoMinimo; // Retorna el tiempo mínimo
    }

    /**
     * Funcion que permite obtener el costo de una multa en un espacio determinado
     * @param idConfiguracion
     * @return
     */
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
            e.printStackTrace();
        }

        return costoMulta; // Retorna el costo de multa
    }

    /**
     * Funcion que permite incertar una reserva en un espacio determinado
     * @param idUsuario el id del usuario que esta reservando
     * @param idEspacio el id del espacio que este usuario esta reservando
     * @param placa la placa del usuario que esta reservando
     * @param tiempoReservado el tiempo que este usuario mantiene el paruqeo reservado
     * @param costo el costo de este espacio
     * @param fechaReserva la fecha en la que esto se reserva
     */
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
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }

    /**
     * Permite insertar en el historial de uso la informacion de este usuario
     * @param idUsuario el id del usuario
     * @param idEspacio el id del espacio
     * @param costo el costo de este
     * @param tiempoOcupado el tiempo ocupado por el usuario
     * @param fechaUso la fecha de uso
     */
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


    /**
     * actualizar estado del espacio parqueado
     * @param numeroEspacio
     */
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

    /**
     * actualizar el estado del vehiculo parqueado
     * @param idUsuario el id del usuario
     * @param placa la placa del usuario
     */
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


    /**
     * OBtener el id del espacio dada la placa del carro que esta parqueado
     * @param placa
     * @return el id del espacio
     */
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

    /**
     * Obtener la fecha de reserva de un espacio dado
     * @param idReserva
     * @return
     */
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

    /**
     * Obtener el tiempo reservado de un espacio dado
     * @param idReserva
     * @return el numero que representa el tiempo de espacio reservado
     */
    public Integer obtenerTiempoReservado(int idReserva) {
        String sql = "SELECT tiempo_reservado FROM Reservas WHERE id_espacio = ?";
        Integer tiempoReservado = null;

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


    /**
     * Actualizar el estado de un vehiculo el cual esta siendo desparuqeado
     * @param placa
     */
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

    /**
     * actualizar el estado de un espacio desparuqado
     * @param numeroEspacio
     */
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

    /**
     * Actualizar el tiempo ocupado por un usuario
     * @param idUsuario el id de este usuario
     * @param nuevoTiempoOcupado el nuevo tiempo que este va a estar ocupando el paruqeo
     * @param fechaReserva la fecha de reserva de este parqueo
     */
    public void actualizarTiempoOcupado(int idUsuario, int nuevoTiempoOcupado, String fechaReserva) {
        String sql = "UPDATE HistorialUso SET tiempo_ocupado = ? WHERE id_usuario = ? AND fecha_uso = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar los valores a cada parámetro de la consulta
            pstmt.setInt(1, nuevoTiempoOcupado);
            pstmt.setInt(2, idUsuario);
            pstmt.setString(3, fechaReserva); // Asegurarse de que el formato de fecha coincide

            // Ejecutar la actualización
            int rowsAffected = pstmt.executeUpdate();

            // Verificar si la actualización fue exitosa
            if (rowsAffected > 0) {
                System.out.println("El tiempo ocupado ha sido actualizado correctamente.");
            } else {
                System.out.println("No se encontró ningún historial de uso con ese usuario o la fecha no coincide.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }

    /**
     * En la tabla costo de los paruqeos, se actualiza el costo
     * @param idUsuario el id del usuario
     * @param nuevoCosto el nuevo costo que se le esta colocando
     * @param fechaReserva la fecha en la que se esta reservando
     */
    public void actualizarCostoEnTablaCosto(int idUsuario, int nuevoCosto, String fechaReserva) {
        String sql = "UPDATE HistorialUso SET costo = ? WHERE id_usuario = ? AND fecha_uso = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Asignar los valores a cada parámetro de la consulta
            pstmt.setInt(1, nuevoCosto);
            pstmt.setInt(2, idUsuario);
            pstmt.setString(3, fechaReserva); // Asegurarse de que el formato de fecha coincide

            // Ejecutar la actualización
            int rowsAffected = pstmt.executeUpdate();

            // Verificar si la actualización fue exitosa
            if (rowsAffected > 0) {
                System.out.println("El costo ha sido actualizado correctamente en la tabla Costo.");
            } else {
                System.out.println("No se encontró ningún registro en la tabla Costo con ese usuario o la fecha no coincide.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }


    /**
     * Se eliminan las reservas en un espacio dado
     * @param idEspacio
     */
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


    public Integer obtenerIdUsuarioPorEspacio(int idEspacio) {
        String sql = "SELECT id_usuario FROM Reservas WHERE id_espacio = ?"; // Asegúrate de que la columna se llama correctamente
        Integer idUsuario = null; // Usamos Integer para permitir que pueda ser nulo

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEspacio); // Establecer el id de espacio como parámetro

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idUsuario = rs.getInt("id_usuario"); // Obtener el id_usuario
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }

        return idUsuario; // Retornar el id_usuario como Integer
    }

    /**
     * Se da un espacio y una placa y se verifica que estas concordan en la reserva
     * @param idEspacio el id del espacio
     * @param placa numero de placa
     * @return true si exite reserva pro ese espacio para esa placa, false en caso contrario
     */
    public boolean existeReservaPorEspacioYPlaca(int idEspacio, String placa) {
        String sql = "SELECT COUNT(*) FROM Reservas WHERE id_espacio = ? AND placa = ?"; // Asegúrate de que las columnas se llaman correctamente
        boolean existe = false;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEspacio); // Establecer el id de espacio como parámetro
            pstmt.setString(2, placa); // Establecer la placa como parámetro

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    existe = rs.getInt(1) > 0; // Si el conteo es mayor que 0, existe
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }

        return existe; // Retornar true si existe, false de lo contrario
    }

    /**
     * Se incerta una multa a un usuario
     * @param idInspector
     * @param placa
     * @param costo
     * @param fechaMultado
     * @return
     */
    public boolean insertarMulta(int idInspector, String placa, int costo, String fechaMultado) {
        String sql = "INSERT INTO Multas (id_inspector, placa, costo, fecha_multado) VALUES (?, ?, ?, ?)";
        boolean exito = false;

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Establecer los parámetros de la consulta
            pstmt.setInt(1, idInspector);
            pstmt.setString(2, placa);
            pstmt.setInt(3, costo);
            pstmt.setString(4, fechaMultado);

            // Ejecutar la inserción
            int filasInsertadas = pstmt.executeUpdate();
            exito = filasInsertadas > 0; // Si se insertó al menos una fila, se considera un éxito
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }

        return exito; // Retornar true si la inserción fue exitosa, false de lo contrario
    }


    /**
     * Se suman los acumulados (En tiempo) de un usuario dado
     * @param idUsuario
     * @param nuevosAcumulados
     */
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

    /**
     * Se aumenta el tiempo de paruqeo para un custo y para una placa que esta parqueada
     * @param placa
     * @param tiempoAumentar
     * @param costoAumentar
     */
    public void aumentarTiempoYCostoPorPlaca(String placa, int tiempoAumentar, int costoAumentar) {
        // SQL para actualizar el tiempo_reservado y el costo
        String sql = "UPDATE Reservas SET tiempo_reservado = tiempo_reservado + ?, costo = costo + ? " +
                "WHERE placa = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Establecer los parámetros para aumentar el tiempo y el costo
            pstmt.setInt(1, tiempoAumentar);
            pstmt.setInt(2, costoAumentar);
            pstmt.setString(3, placa);

            // Imprimir la consulta para depuración
            System.out.println("Ejecutando la actualización para la placa: " + placa);
            System.out.println("Tiempo a aumentar: " + tiempoAumentar + ", Costo a aumentar: " + costoAumentar);

            // Ejecutar la actualización
            int rowsAffected = pstmt.executeUpdate();

            // Verificar si la actualización fue exitosa
            if (rowsAffected > 0) {
                System.out.println("El tiempo y el costo han sido aumentados correctamente para la placa: " + placa);
            } else {
                System.out.println("No se encontró ninguna reserva para la placa: " + placa);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Manejar el error
        }
    }


















}
