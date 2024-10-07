package com.example.parqueoscallejeros;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


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
        if (verificarConexion()) {
            welcomeText.setText("Conexión a la base de datos exitosa!");
        } else {
            welcomeText.setText("Error al conectar con la base de datos!");
        }
    }
    private boolean verificarConexion() {
        try (Connection conn = DriverManager.getConnection(DB_URL)) {
            if (conn != null) {
                return true; // Conexión fue exitosa
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Puedes registrar el error para más detalles
        }
        return false; // Si algo falla, la conexión no fue exitosa
    }
}