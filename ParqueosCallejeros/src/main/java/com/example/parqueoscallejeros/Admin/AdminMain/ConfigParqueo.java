package com.example.parqueoscallejeros.Admin.AdminMain;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfigParqueo {
    private int uniqueId;
    private String userId;
    private String userPin;

    @FXML
    private ComboBox<Integer> startHour;

    @FXML
    private ComboBox<Integer> startMinute;

    @FXML
    private ComboBox<Integer> endHour;

    @FXML
    private ComboBox<Integer> endMinute;

    @FXML
    private TextField precioHora;

    @FXML
    private TextField minCompra;

    @FXML
    private TextField costoMulta;

    @FXML
    private TextField espaciosInicio;

    @FXML
    private TextField espaciosFin;

    @FXML
    private Label statusLabel;

    @FXML
    private Button submitButton;

    // Este método se llama automáticamente al cargar la escena
    @FXML
    public void initialize() {
        // Rellenar las horas (0 a 23)
        for (int i = 0; i < 24; i++) {
            startHour.getItems().add(i);
            endHour.getItems().add(i);
        }

        // Rellenar los minutos (0 a 55 en incrementos de 5 minutos)
        for (int i = 0; i < 60; i += 5) {
            startMinute.getItems().add(i);
            endMinute.getItems().add(i);
        }

        // Opcional: establecer valores predeterminados
        /**
        startHour.setValue(8); // Hora predeterminada de inicio
        startMinute.setValue(0);
        endHour.setValue(17); // Hora predeterminada de fin
        endMinute.setValue(0);
         **/
    }


    // Método para recibir los datos del usuario
    public void setUserData(int id, String userId, String userPin) {
        this.uniqueId = id;
        this.userId = userId;
        this.userPin = userPin;

        System.out.println("ID único: " + uniqueId);
        System.out.println("ID usuario: " + userId);
        System.out.println("PIN usuario: " + userPin);
    }


    public void handleInformation(ActionEvent event) throws IOException { // REGISTRO
        validacionDatos();
    }

    private void validacionDatos() {
        boolean isValid = true;

        // Validación de horario de regulación
        Integer startH = startHour.getValue();
        Integer startM = startMinute.getValue();
        Integer endH = endHour.getValue();
        Integer endM = endMinute.getValue();

        if (startH == null || startM == null || endH == null || endM == null) {
            statusLabel.setText("Error: Debes seleccionar tanto la hora de inicio como la de fin.");
            isValid = false;
        } else if (startH > endH || (startH.equals(endH) && startM >= endM)) {
            statusLabel.setText("Error: La hora de fin debe ser mayor que la hora de inicio.");
            isValid = false;
        }

        // Validación de precio por hora (debe ser un entero par)
        try {
            int precio = Integer.parseInt(precioHora.getText());
            if (precio % 2 != 0) {
                statusLabel.setText("Error: El precio por hora debe ser un número entero par.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Error: El precio por hora debe ser un número entero.");
            isValid = false;
        }

        // Validación de tiempo mínimo de compra (debe ser un entero)
        try {
            int min = Integer.parseInt(minCompra.getText());
            if (min <= 0) {
                statusLabel.setText("Error: El tiempo mínimo de compra debe ser un número positivo.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Error: El tiempo mínimo de compra debe ser un número entero.");
            isValid = false;
        }

        // Validación del costo de multa (debe ser un entero)
        try {
            int multa = Integer.parseInt(costoMulta.getText());
            if (multa <= 0) {
                statusLabel.setText("Error: El costo de la multa debe ser un número entero positivo.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Error: El costo de la multa debe ser un número entero.");
            isValid = false;
        }

        // Validación de espacios de parqueo (los espacios deben ser números de 1 a 5 dígitos)
        try {
            int inicioEspacio = Integer.parseInt(espaciosInicio.getText());
            int finEspacio = Integer.parseInt(espaciosFin.getText());

            if (String.valueOf(inicioEspacio).length() > 5 || String.valueOf(finEspacio).length() > 5) {
                statusLabel.setText("Error: Los números de los espacios deben tener entre 1 y 5 dígitos.");
                isValid = false;
            }

            if (inicioEspacio >= finEspacio) {
                statusLabel.setText("Error: El espacio de inicio debe ser menor que el espacio de fin.");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            statusLabel.setText("Error: Los espacios de parqueo deben ser números enteros.");
            isValid = false;
        }

        // Si todas las validaciones son correctas, se puede proceder
        if (isValid) {
            statusLabel.setText("Todos los datos son válidos.");
            // Aquí puedes agregar la lógica para continuar, como guardar los datos
        }
    }
}
