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

    }

}

// cambio