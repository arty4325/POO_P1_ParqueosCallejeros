package com.example.parqueoscallejeros.Admin.AdminLogin;

import com.example.parqueoscallejeros.EnvioCorreos;
import com.example.parqueoscallejeros.dataBase.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

public class RegisterAndLogin {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private TextField regNombre;
    @FXML
    private TextField regApellidos;
    @FXML
    private TextField regTelefono;
    @FXML
    private TextField regCorreo;
    @FXML
    private TextField regDireccion;
    @FXML
    private TextField regIdentificacion;
    @FXML
    private TextField regPin;
    @FXML
    private Label signLabel;
    @FXML
    private TextField idAdminVal;
    @FXML
    private TextField idAdminPinVal;
    @FXML
    private TextField idAdminCodVal;
    @FXML
    private Label verificationLabel;


    public void switchToSignIn(ActionEvent event) throws IOException { // REGISTRO
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminLogin/Scene2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToVerify(ActionEvent event) throws IOException { // REGISTRO
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminLogin/Scene3.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void sendSignIn(ActionEvent event) throws IOException { // REGISTRO
        if(validarDatosRegistro()){
            DatabaseManager databaseManager = new DatabaseManager();
            Random random = new Random();
            int validacion = random.nextInt(900) + 100; // Genera un número entre 100 y 999
            System.out.println("Código de validación: " + validacion); // este es el número de validación
            String validacionString = Integer.toString(validacion);

            LocalDate fechaActual = LocalDate.now();


            try {
                databaseManager.insertarAdministrador(regNombre.getText()
                , regApellidos.getText(), regTelefono.getText(), regCorreo.getText(),
                regDireccion.getText(), fechaActual, regIdentificacion.getText(), regPin.getText(), validacionString);
                String message =
                        "<p>Bienvenido a la aplicación de parqueos callejeros, " + regNombre.getText() + ",</p>" +
                                "<p>Este correo incluye el código de verificación que usted debe ingresar para activar su usuario de administrador.</p>" +
                                "<p>El código de verificación suyo es: <strong>" + validacionString + "</strong></p>" +
                                "<p>Muchas gracias por confiar en nosotros.</p>" +
                                "<p>Atentamente,<br>Parqueos Callejeros S.A</p>";

                EnvioCorreos envioCorreos = new EnvioCorreos();
                envioCorreos.createEmail(regCorreo.getText(), "Confirmacion Correos Callejeros", message);
                envioCorreos.sendEmail();
                signLabel.setText("Usuario Registrado");
                // Me voy a otra ventana en donde verifico que todo este bien
            } catch (Exception e) {
                // Captura cualquier excepción que ocurra durante la inserción en la base de datos
                //infoLabel.setText("No se pudo subir la información a la base de datos.");
                signLabel.setText("No se pudo subir la informacion en la base de datos");
                System.err.println("Error al insertar el usuario: " + e.getMessage());
                e.printStackTrace(); // Para imprimir el error completo en la consola
            }
        }

    }

    public void sendVerificationInfo(ActionEvent event) throws IOException {
        DatabaseManager databaseManager = new DatabaseManager();
        if(databaseManager.verificarAdministrador(idAdminVal.getText(), idAdminPinVal.getText(), idAdminCodVal.getText())){
            verificationLabel.setText("Se ha registrado el administrador");
        } else {
            verificationLabel.setText("No se ha verificado el administrador");
        }
    }

    public boolean validarDatosRegistro() {
        // Validar nombre
        if (regNombre.getText() == null || regNombre.getText().isEmpty()) {
            signLabel.setText("El nombre no puede estar vacío.");
            return false;
        }

        // Validar apellidos
        if (regApellidos.getText() == null || regApellidos.getText().isEmpty()) {
            signLabel.setText("El apellido no puede estar vacío.");
            return false;
        }

        // Validar teléfono (8 dígitos)
        if (regTelefono.getText() == null || !regTelefono.getText().matches("\\d{8}")) {
            signLabel.setText("El teléfono debe contener 8 dígitos.");
            return false;
        }

        // Validar correo (formato simple)
        if (regCorreo.getText() == null || !regCorreo.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")) {
            signLabel.setText("Correo inválido.");
            return false;
        }

        // Validar dirección
        if (regDireccion.getText() == null || regDireccion.getText().isEmpty()) {
            signLabel.setText("La dirección no puede estar vacía.");
            return false;
        }

        // Validar identificación (debe tener entre 2 y 25 caracteres)
        if (regIdentificacion.getText() == null || !regIdentificacion.getText().matches(".{2,25}")) {
            signLabel.setText("La identificación debe contener entre 2 y 25 caracteres.");
            return false;
        }

        // Validar PIN (4 dígitos)
        if (regPin.getText() == null || !regPin.getText().matches("\\d{4}")) {
            signLabel.setText("El PIN debe contener 4 dígitos.");
            return false;
        }

        // Si pasa todas las validaciones
        signLabel.setText("");
        return true;
    }


}
