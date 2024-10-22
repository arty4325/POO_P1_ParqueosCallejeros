package com.example.parqueoscallejeros.Admin.AdminLogin;
// cambio
import com.example.parqueoscallejeros.Admin.AdminMain.MainController;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Random;

/**
 * Clase que permite hacer el registro y el login para el administrador
 */
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
    private PasswordField regPin;
    @FXML
    private Label signLabel;
    @FXML
    private TextField idAdminVal;
    @FXML
    private PasswordField idAdminPinVal;
    @FXML
    private PasswordField idAdminCodVal;
    @FXML
    private TextField sendAdminUser;
    @FXML
    private PasswordField sendAdminPassword;
    @FXML
    private Label verificationLabel;
    @FXML
    private TextField usuarioCambio;
    @FXML
    private TextField cambioIdentificacion;
    @FXML
    private PasswordField cambioCodigo;
    @FXML
    private PasswordField cambioPin;


    /**
     * Permite volver a la pantalla main
     * @param event
     * @throws IOException
     */
    public void switchToMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminLogin/Scene1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Permite moverse a la pantalla de inicio de sesion
     * @param event
     * @throws IOException
     */
    public void switchToSignIn(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminLogin/Scene2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void switchToLogIN(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminLogin/Scene4.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * Permite volver a la ventana para verificar usuario
     * @param event
     * @throws IOException
     */
    public void switchToVerify(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminLogin/Scene3.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Permite moverse a la quinta pantalla de configuracion
     * @param event
     * @throws IOException
     */
    public void switchToScene5(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminLogin/Scene5.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Permite moverse a la sexta pantalla de configuracion
     * @param event
     * @throws IOException
     */
    public void switchToScene6(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminLogin/Scene6.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Envia a la base de datos la informacion de incio de sesion
     * @param event
     * @throws IOException
     */
    public void sendSignIn(ActionEvent event) throws IOException {
        if(validarDatosRegistro()){ // Se corre una funcion que valida que las entradas sean correctas
            DatabaseManager databaseManager = new DatabaseManager(); // Se instancia el manejador de base de datos
            Random random = new Random(); // Se crea un numero random
            int validacion = random.nextInt(900) + 100; // Genera un número entre 100 y 999
            System.out.println("Código de validación: " + validacion); // Este numero random el usuario no lo ve y es el numero de validacion
            String validacionString = Integer.toString(validacion);

            LocalDate fechaActual = LocalDate.now(); // Se consigue la fecha actual del sistema


            try { // Se intenta registrar el administrador en la base de datos
                databaseManager.insertarAdministrador(regNombre.getText()
                , regApellidos.getText(), regTelefono.getText(), regCorreo.getText(),
                regDireccion.getText(), fechaActual, regIdentificacion.getText(), regPin.getText(), validacionString);
                String message =
                        "<p>Bienvenido a la aplicación de parqueos callejeros, " + regNombre.getText() + ",</p>" +
                                "<p>Este correo incluye el código de verificación que usted debe ingresar para activar su usuario de administrador.</p>" +
                                "<p>El código de verificación suyo es: <strong>" + validacionString + "</strong></p>" +
                                "<p>Muchas gracias por confiar en nosotros.</p>" +
                                "<p>Atentamente,<br>Parqueos Callejeros S.A</p>";

                EnvioCorreos envioCorreos = new EnvioCorreos(); // Se envia el correo electronico de verificacion despues de registrar
                envioCorreos.createEmail(regCorreo.getText(), "Confirmacion Correos Callejeros", message);
                envioCorreos.sendEmail();
                signLabel.setText("Usuario Registrado");
                // Me voy a otra ventana en donde verifico que todo este bien

            } catch (Exception e) {
                // Captura cualquier excepción que ocurra durante la inserción en la base de datos
                signLabel.setText("No se pudo subir la informacion en la base de datos");
                System.err.println("Error al insertar el usuario: " + e.getMessage());
                e.printStackTrace(); // Para imprimir el error completo en la consola
            }
        }

    }

    /**
     * Funcion que envia la informacion de verificacion a la base de datos
     * @param event
     * @throws IOException
     */
    public void sendVerificationInfo(ActionEvent event) throws IOException {
        DatabaseManager databaseManager = new DatabaseManager(); // Se intancia el manejador de base de datos
        if(databaseManager.verificarAdministrador(idAdminVal.getText(), idAdminPinVal.getText(), idAdminCodVal.getText())){ // Se verifica el administrador en la db
            verificationLabel.setText("Se ha registrado el administrador"); // Se coloca en pantalla un mensaje de feedback
        } else {
            verificationLabel.setText("No se ha verificado el administrador");
        }
    }

    /**
     * Funcion que permite iniciar sesion en la base de datos
     * @param event
     * @throws IOException
     */
    public void handleSignIn(ActionEvent event) throws IOException { // Esto permite iniciar sesion
        DatabaseManager databaseManager = new DatabaseManager(); // Se instancia el manejador de la base de datos
        if(databaseManager.iniciarSesionAdmin(sendAdminUser.getText(), sendAdminPassword.getText())){ // Se le envia el nombre de usuario y la contra
            int id = databaseManager.obtenerIdAdmin(sendAdminUser.getText(), sendAdminPassword.getText()); // Se obtiene el id unico para ese admin

            // Se abre la pantalla main de admin main
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminMainFunctions/AdminMain.fxml"));
            Parent root = loader.load();

            // Obtén el controlador del nuevo FXML
            MainController controller = loader.getController();

            // Pasa el valor (por ejemplo, el idUsuario)
            controller.setUserData(id, sendAdminUser.getText(), sendAdminPassword.getText());

            // Cambia la escena
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            System.out.println("No se ha logrado hacer el inicio de sesion");

        }
    }

    /**
     * Esto permite validar que los datos en los entrys es correcto
     * @return Devuelve feedback de la estructura de los entrys
     */
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

    /**
     * Manejador que permite manejar el cambio de informacion
     * @param event
     * @throws IOException
     * @throws MessagingException
     */
    public void handleChangeData(ActionEvent event) throws IOException, MessagingException {
        Random random = new Random();
        int validacion = random.nextInt(900) + 100; // Obtiene un numero random que se usa para cambiar la informacion

        DatabaseManager databaseManager = new DatabaseManager(); // Se instancia el manejador de base de datos
        databaseManager.actualizarCodigoCambioAdministrador(usuarioCambio.getText(), validacion); // Se envia la informacoin de cambio de usurio y el codigo de validacion
        String validacionString;
        validacionString = Integer.toString(validacion);

        String correo = databaseManager.obtenerCorreoAdministrador(usuarioCambio.getText());
        String message =
                "<p>Bienvenido a la aplicación de correos callejeros, " + usuarioCambio.getText() + ",</p>" +
                        "<p>Usted solicito un codigo para poder cambiar su contraseña en su usuario de ADMINISTRADOR.</p>" +
                        "<p>El código de cambio suyo es: <strong>" + validacionString + "</strong></p>" +
                        "<p>Muchas gracias por confiar en nosotros.</p>" +
                        "<p>Atentamente,<br>Parqueos Callejeros S.A</p>";

        EnvioCorreos envioCorreos = new EnvioCorreos(); // Se envia un correo electronico de verificacion de esta accion
        envioCorreos.createEmail(correo, "Cambio de Contraseña " + usuarioCambio.getText(), message);
        envioCorreos.sendEmail();


        Parent root = FXMLLoader.load(getClass().getResource("/com/example/parqueoscallejeros/User/UserLogin/Scene6.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Maneja el cambio de pin
     * @param event
     * @throws IOException
     */
    public void handleSetNewPin(ActionEvent event) throws IOException {
        DatabaseManager databaseManager = new DatabaseManager(); // Se instancia el manejador de base de datos

        String identificacion = cambioIdentificacion.getText();
        String codigoTexto = cambioCodigo.getText();
        String nuevoPin = cambioPin.getText();
        // Verificar que el código de cambio contenga solo números
        if (!codigoTexto.matches("\\d+")) {
            // Mostrar un mensaje de error si el código no es válido
            System.out.println("El código de cambio debe contener solo números.");
            // Aquí puedes usar un dialogo de alerta o una etiqueta en la interfaz para mostrar el mensaje
            return; // Salir del método si la verificación falla
        }

        // Convertir el código de cambio a int
        int codigoCambio = Integer.parseInt(codigoTexto);

        // Llamar al método para cambiar el PIN
        boolean pinCambiado = databaseManager.cambiarPinAdministrador(identificacion, codigoCambio, nuevoPin);
        databaseManager.resetearCodigoCambioAdministrador(identificacion);
        if (pinCambiado) {
            System.out.println("El PIN ha sido cambiado exitosamente.");
        } else {
            System.out.println("Error al cambiar el PIN. Verifica los datos proporcionados.");
        }

    }



}
