package com.example.parqueoscallejeros.User.UserLogin;

import com.example.parqueoscallejeros.EnvioCorreos;
import com.example.parqueoscallejeros.User.UserMain.MainController;
import com.example.parqueoscallejeros.dataBase.DatabaseManager;
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

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

public class RegisterAndLogin {

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


    @FXML
    private Label verifyLabel;

    @FXML
    private Label signInLabel;

    @FXML
    private TextField idUsuarioVal;

    @FXML
    private TextField idPinVal;

    @FXML
    private TextField idCodigoVal;

    @FXML
    private TextField inicUsuario;

    @FXML
    private TextField inicContra;


    public void switchToScene1(ActionEvent event) throws IOException { // INICIO
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/User/UserLogin/Scene1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene2(ActionEvent event) throws IOException { // REGISTRO
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/User/UserLogin/Scene2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene3(ActionEvent event) throws IOException { // REGISTRO
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/User/UserLogin/Scene3.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToScene4(ActionEvent event) throws IOException { // REGISTRO
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/User/UserLogin/Scene4.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void handleSignIn(ActionEvent event) throws IOException {
        DatabaseManager databaseManager = new DatabaseManager();
        if(databaseManager.iniciarSesion(inicUsuario.getText(), inicContra.getText())){
            System.out.println("Se ha iniciado correctamente");

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/User/UserMainFunctions/UsuarioMain.fxml"));
            Parent root = loader.load();

            // Obtén el controlador del nuevo FXML
            MainController controller = loader.getController();

            // Pasa el valor (por ejemplo, el idUsuario)
            controller.setUserData(inicUsuario.getText(), inicContra.getText());

            // Cambia la escena
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("No se ha logrado hacer el inicio de sesion");
            signInLabel.setText("No se ha logrado hacer el inicio de sesion");
        }
    }

    public void sendVerificationInfo(ActionEvent event) throws IOException {
        // Aqui tengo que programar lo que pasa cuando se envia la informacion de la validacion
        /**
        System.out.println(idUsuarioVal.getText());
        System.out.println(idPinVal.getText());
        System.out.println(idCodigoVal.getText());
         **/

        DatabaseManager databaseManager = new DatabaseManager();
        if(databaseManager.verificarUsuario(idUsuarioVal.getText(), idPinVal.getText(), idCodigoVal.getText())){
            //System.out.println("VALIDADO");
            verifyLabel.setText("Se logro validar con exito el usuario");
        } else {
            verifyLabel.setText("Los datos de validacion no son correctos");
        }

    }

    public void handleData(ActionEvent event) throws MessagingException {
        if (validarDatos()) {
            System.out.println("Datos válidos");
            DatabaseManager databaseManager = new DatabaseManager();

            Random random = new Random();
            int validacion = random.nextInt(900) + 100; // Genera un número entre 100 y 999
            System.out.println("Código de validación: " + validacion); // este es el número de validación
            String validacionString = Integer.toString(validacion);

            LocalDate fechaActual = LocalDate.now();
            System.out.println("Fecha actual: " + fechaActual.toString());

            try {
                // Intentar insertar el usuario en la base de datos
                databaseManager.insertarUsuario(
                        nombreUsuario.getText(),
                        apellidoUsuario.getText(),
                        telefonoUsuario.getText(),
                        correoUsuario.getText(),
                        direccionUsuario.getText(),
                        tarjetaUsuario.getText(),
                        String.valueOf(vencimientoTarjeta.getValue()),
                        validacionString,
                        fechaActual,
                        idUsuario.getText(),
                        pinUsuario.getText(),
                        0
                );
                // Si la inserción es exitosa, puedes mostrar un mensaje de éxito
                // Si la inserción es exitosa, puedes mostrar un mensaje de éxito
                String message =
                        "<p>Bienvenido a la aplicación de correos callejeros, " + nombreUsuario.getText() + ",</p>" +
                        "<p>Este correo incluye el código de verificación que usted debe ingresar para activar su usuario.</p>" +
                        "<p>El código de verificación suyo es: <strong>" + validacionString + "</strong></p>" +
                        "<p>Muchas gracias por confiar en nosotros.</p>" +
                        "<p>Atentamente,<br>Parqueos Callejeros S.A</p>";

                EnvioCorreos envioCorreos = new EnvioCorreos();
                envioCorreos.createEmail(correoUsuario.getText(), "Confirmacion Correos Callejeros", message);
                envioCorreos.sendEmail();
                infoLabel.setText("Usuario registrado correctamente.");
                // Aqui me tengo que ir a la ventana que me permite autenticar el usuario
                Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/User/UserLogin/Scene3.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                // Captura cualquier excepción que ocurra durante la inserción en la base de datos
                infoLabel.setText("No se pudo subir la información a la base de datos.");
                System.err.println("Error al insertar el usuario: " + e.getMessage());
                e.printStackTrace(); // Para imprimir el error completo en la consola
            }
        } else {
            // Si los datos no son válidos, mostrar un mensaje al usuario
            validarDatos();
        }
    }




    // Método para validar los datos del formulario
    public boolean validarDatos() {
        // Validar nombre
        if (nombreUsuario.getText() == null || nombreUsuario.getText().isEmpty()) {
            //System.out.println("El nombre no puede estar vacío.");
            infoLabel.setText("El nombre no puede estar vacío.");
            return false;
        }

        // Validar apellidos
        if (apellidoUsuario.getText() == null || apellidoUsuario.getText().isEmpty()) {
            //System.out.println("El apellido no puede estar vacío.");
            infoLabel.setText("El apellido no puede estar vacío.");
            return false;
        }

        // Validar teléfono (8 dígitos)
        if (telefonoUsuario.getText() == null || !telefonoUsuario.getText().matches("\\d{8}")) {
            //System.out.println("El teléfono debe contener 8 dígitos.");
            infoLabel.setText("El teléfono debe contener 8 dígitos.");
            return false;
        }

        // Validar correo (formato simple)
        if (correoUsuario.getText() == null || !correoUsuario.getText().matches("^[\\w.-]+@[\\w.-]+\\.[a-z]{2,}$")) {
            //System.out.println("Correo inválido.");
            infoLabel.setText("Correo inválido.");
            return false;
        }

        // Validar dirección
        if (direccionUsuario.getText() == null || direccionUsuario.getText().isEmpty()) {
            //System.out.println("La dirección no puede estar vacía.");
            infoLabel.setText("La dirección no puede estar vacía.");
            return false;
        }

        // Validar tarjeta de crédito (16 dígitos)
        if (tarjetaUsuario.getText() == null || !tarjetaUsuario.getText().matches("\\d{16}")) {
            //System.out.println("La tarjeta de crédito debe contener 16 dígitos.");
            infoLabel.setText("La tarjeta de crédito debe contener 16 dígitos.");
            return false;
        }

        // Validar fecha de vencimiento de la tarjeta
        if (vencimientoTarjeta.getValue() == null) {
            //System.out.println("La fecha de vencimiento de la tarjeta no puede estar vacía.");
            infoLabel.setText("La fecha de vencimiento de la tarjeta no puede estar vacía.");
            return false;
        }

        // Validar el código de usuario (debe tener entre 2 y 25 caracteres)
        if (idUsuario.getText() == null || !idUsuario.getText().matches(".{2,25}")) {
            //System.out.println("El código de usuario debe contener entre 2 y 25 caracteres.");
            infoLabel.setText("La identificacion de usuario debe contener entre 2 y 25 caracteres.");
            return false;
        }


        // Validar PIN (4 dígitos)
        if (pinUsuario.getText() == null || !pinUsuario.getText().matches("\\d{4}")) {
            //System.out.println("El PIN debe contener 4 dígitos.");
            infoLabel.setText("El PIN debe contener 4 dígitos.");
            return false;
        }

        // Si pasa todas las validaciones
        infoLabel.setText("");
        return true;
    }



}
