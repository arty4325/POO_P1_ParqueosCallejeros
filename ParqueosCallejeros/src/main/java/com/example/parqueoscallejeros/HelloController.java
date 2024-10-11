package com.example.parqueoscallejeros;

import com.example.parqueoscallejeros.dataBase.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;


public class HelloController {
    @FXML
    private Label welcomeText;

    // Ahora le doy la ruta a la base de datos
    private static final String DB_URL = "jdbc:sqlite:databases/parqueoscallejeros.db"; // Ruta relativa

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    protected void onBotonPrueba() {
        // Ejemplo de datos para insertar
        String nombre = "Oscar";
        String apellido = "Acuna";
        String telefono = "71732204"; // Debe ser un número de 8 dígitos
        String correo = "arturo@acuna.com"; // Asegúrate de que el formato sea correcto
        String direccion = "Calle Falsa 123"; // Dirección de ejemplo
        LocalDate fechaIngreso = LocalDate.now(); // Fecha actual
        String identificacionUsuario = "fernando4325"; // Debe ser único y entre 2 a 25 caracteres
        String pin = "9904"; // Debe ser un PIN de 4 caracteres

        // Crear una instancia de DatabaseManager
        DatabaseManager dbManager = new DatabaseManager();

        // Intentar insertar el registro
        if (dbManager.insertarAdministrador(nombre, apellido, telefono, correo, direccion, fechaIngreso, identificacionUsuario, pin)) {
            welcomeText.setText("Registro insertado exitosamente!");
        } else {
            welcomeText.setText("Error al insertar el registro!");
        }
    }

}