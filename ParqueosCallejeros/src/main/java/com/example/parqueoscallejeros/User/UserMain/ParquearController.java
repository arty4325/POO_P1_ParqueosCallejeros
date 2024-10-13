package com.example.parqueoscallejeros.User.UserMain;

import com.example.parqueoscallejeros.dataBase.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ParquearController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private boolean isFree = false;

    private int uniqueId;
    private String userId;
    private String userPin;

    @FXML
    private ComboBox<String> placasAccordion; // Cambiar el tipo a String si las placas son cadenas

    @FXML
    private TextArea espaciosDisponibles;

    @FXML
    private TextField espacioParqueo;

    @FXML
    private Label infoEspacio;

    @FXML
    private Label infoGratis;


    public void verificarParqueo(ActionEvent event) throws IOException { // REGISTRO
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            int numero = Integer.parseInt(espacioParqueo.getText()); // Convertir String a int
            if(databaseManager.estaEspacioDisponible(numero)){
                espacioParqueo.setDisable(true);
                infoEspacio.setText("Espacio elegido disponible");
                String horarioInicio = databaseManager.obtenerHorarioInicio(numero);
                String horarioFin = databaseManager.obtenerHorarioFin(numero);
                // Definir el formato de tiempo que estás usando
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

                // Obtener la hora actual del sistema
                LocalTime currentTime = LocalTime.now();

                // Convertir los horarios de String a LocalTime
                LocalTime startTime = LocalTime.parse(horarioInicio, formatter);
                LocalTime endTime = LocalTime.parse(horarioFin, formatter);

                // Verificar si la hora actual está dentro del rango
                if(!( (currentTime.isAfter(startTime) || currentTime.equals(startTime)) &&
                        (currentTime.isBefore(endTime) || currentTime.equals(endTime)) )){
                    isFree = true;
                    infoGratis.setText("En este horario el parqueo es gratis!");
                }


                /**
                String horarioInicio = databaseManager.obtenerHorarioInicio(numero);
                String horarioFin = databaseManager.obtenerHorarioFin(numero);

                System.out.println(horarioInicio);
                System.out.println(horarioFin);
                System.out.println(databaseManager.obtenerPrecioPorHora(numero));
                System.out.println(databaseManager.obtenerTiempoMinimo(numero));
                System.out.println(databaseManager.obtenerCostoMulta(numero));
                 **/
            } else {
                espacioParqueo.setDisable(false);
                infoEspacio.setText("Espacio no disponible");
            }
        } catch (NumberFormatException e) {
            infoEspacio.setText("Espacio elegido no esta disponible");
        }


    }
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

        //espacioParqueo.setDisable(true);
    }

    // Método para cargar placas en el ComboBox
    private void cargarPlacas() {
        List<String> placas = obtenerPlacasPorUsuario(uniqueId); // Llama al método que obtiene las placas
        placasAccordion.getItems().clear(); // Limpiar elementos existentes

        // Agregar placas al ComboBox
        for (String placa : placas) {
            placasAccordion.getItems().add(placa);
        }

        placasAccordion.getSelectionModel().select(0);
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
