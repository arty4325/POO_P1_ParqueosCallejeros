package com.example.parqueoscallejeros.User.UserMain;

import com.example.parqueoscallejeros.dataBase.DatabaseManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.List;

public class ParquearController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private int uniqueId;
    private String userId;
    private String userPin;

    @FXML
    private ComboBox<String> placasAccordion; // Cambiar el tipo a String si las placas son cadenas

    @FXML
    private TextArea espaciosDisponibles;

    public void setUserData(int id, String userId, String userPin) {
        System.out.println("PARQUEAR CONTROLLER");
        System.out.println(id);
        System.out.println(userId);
        System.out.println(userPin);
        this.uniqueId = id;
        this.userId = userId;
        this.userPin = userPin;
        cargarPlacas();
        cargarEspaciosDisponibles();
    }

    // Método para cargar placas en el ComboBox
    private void cargarPlacas() {
        List<String> placas = obtenerPlacasPorUsuario(uniqueId); // Llama al método que obtiene las placas
        placasAccordion.getItems().clear(); // Limpiar elementos existentes

        // Agregar placas al ComboBox
        for (String placa : placas) {
            placasAccordion.getItems().add(placa);
        }
    }

    private void cargarEspaciosDisponibles() {
        List<Integer> listaEspacios = obtenerEspaciosDisponibles();
        espaciosDisponibles.clear();
        String label = "";

        for(Integer espacio: listaEspacios) {
            String espacioString = Integer.toString(espacio);
            label += espacioString + "\n";
            //espaciosDisponibles.setText(espacioString);
        }
        espaciosDisponibles.setText(label);
    }

    // Método que simula la obtención de placas por usuario (debe ser implementado)
    private List<String> obtenerPlacasPorUsuario(int idUsuario) {
        System.out.println("hola" + idUsuario);
        DatabaseManager databaseManager = new DatabaseManager();
        return databaseManager.obtenerPlacasPorUsuario(idUsuario);// Reemplaza esto con la lógica de tu base de datos
    }

    private List<Integer> obtenerEspaciosDisponibles() {
        DatabaseManager databaseManager = new DatabaseManager();
        return databaseManager.obtenerEspaciosDisponibles();
    }
}
