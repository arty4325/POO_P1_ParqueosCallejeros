package com.example.parqueoscallejeros.User.UserLogin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class Scene1 {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField nombreUsuario;

    @FXML
    private TextField apellidoUsuario;

    @FXML
    private TextField telefonoUsuario;

    @FXML
    private TextField correoUsuario;

    @FXML
    private TextField direccionUsuario;

    @FXML
    private TextField tarjetaUsuario;

    @FXML
    private DatePicker vencimientoTarjeta;

    @FXML
    private TextField idUsuario;

    @FXML
    private TextField pinUsuario;

    @FXML
    private Label infoLabel;

    public void switchToScene1(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/User/UserLogin/Scene1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene2(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/User/UserLogin/Scene2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleData(ActionEvent event) {
        //nombreUsuario.getText();
        /**
        System.out.println(nombreUsuario.getText());
        System.out.println(apellidoUsuario.getText());
        System.out.println(telefonoUsuario.getText());
        System.out.println(correoUsuario.getText());
        System.out.println(direccionUsuario.getText());
        System.out.println(tarjetaUsuario.getText());
        System.out.println(vencimientoTarjeta.getValue());
        System.out.println(idUsuario.getText());
        System.out.println(pinUsuario.getText());
         **/
        validarDatos();
    }

    // Método para validar los datos del formulario
    public boolean validarDatos() {
        // Validar nombre
        if (nombreUsuario.getText() == null || nombreUsuario.getText().isEmpty()) {
            System.out.println("El nombre no puede estar vacío.");
            infoLabel.setText("El nombre no puede estar vacío.");
            return false;
        }

        // Validar apellidos
        if (apellidoUsuario.getText() == null || apellidoUsuario.getText().isEmpty()) {
            System.out.println("El apellido no puede estar vacío.");
            infoLabel.setText("El apellido no puede estar vacío.");
            return false;
        }

        // Validar teléfono (8 dígitos)
        if (telefonoUsuario.getText() == null || !telefonoUsuario.getText().matches("\\d{8}")) {
            System.out.println("El teléfono debe contener 8 dígitos.");
            infoLabel.setText("El teléfono debe contener 8 dígitos.");
            return false;
        }

        // Validar correo (formato simple)
        if (correoUsuario.getText() == null || !correoUsuario.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")) {
            System.out.println("Correo inválido.");
            infoLabel.setText("Correo inválido.");
            return false;
        }

        // Validar dirección
        if (direccionUsuario.getText() == null || direccionUsuario.getText().isEmpty()) {
            System.out.println("La dirección no puede estar vacía.");
            infoLabel.setText("La dirección no puede estar vacía.");
            return false;
        }

        // Validar tarjeta de crédito (16 dígitos)
        if (tarjetaUsuario.getText() == null || !tarjetaUsuario.getText().matches("\\d{16}")) {
            System.out.println("La tarjeta de crédito debe contener 16 dígitos.");
            infoLabel.setText("La tarjeta de crédito debe contener 16 dígitos.");
            return false;
        }

        // Validar fecha de vencimiento de la tarjeta
        if (vencimientoTarjeta.getValue() == null) {
            System.out.println("La fecha de vencimiento de la tarjeta no puede estar vacía.");
            infoLabel.setText("La fecha de vencimiento de la tarjeta no puede estar vacía.");
            return false;
        }

        // Validar código de validación (3 dígitos)
        if (idUsuario.getText() == null || !idUsuario.getText().matches("\\d{3}")) {
            System.out.println("El código de validación debe contener 3 dígitos.");
            infoLabel.setText("El código de validación debe contener 3 dígitos.");
            return false;
        }

        // Validar PIN (4 dígitos)
        if (pinUsuario.getText() == null || !pinUsuario.getText().matches("\\d{4}")) {
            System.out.println("El PIN debe contener 4 dígitos.");
            infoLabel.setText("El PIN debe contener 4 dígitos.");
            return false;
        }

        // Si pasa todas las validaciones
        return true;
    }



}
