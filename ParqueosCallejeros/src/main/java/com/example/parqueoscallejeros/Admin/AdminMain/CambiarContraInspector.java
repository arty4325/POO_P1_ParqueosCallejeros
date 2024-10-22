package com.example.parqueoscallejeros.Admin.AdminMain;

import com.example.parqueoscallejeros.EnvioCorreos;
import com.example.parqueoscallejeros.dataBase.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Random;

public class CambiarContraInspector {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private int uniqueId;
    private String userId;
    private String userPin;


    @FXML
    private TextField inspectorCargado;

    @FXML
    private Label infoInspector;

    @FXML
    private TextField codigoValidacion;

    @FXML
    private TextField nuevoPin;

    @FXML
    private Label infoSend;

    /**
     * Permite configurar la informacion del usuario que proviene de la pantalla anterior
     * @param id id del usuario
     * @param userId id alfanumerico
     * @param userPin pin del usuario
     */
    public void setUserData(int id, String userId, String userPin) {
        System.out.println(id);
        System.out.println(userId);
        System.out.println(userPin);
        this.uniqueId = id;
        this.userId = userId;
        this.userPin = userPin;
        codigoValidacion.setDisable(true);
        nuevoPin.setDisable(true);
    }

    /**
     * Funcion que permite moverse a admin
     * @param event
     * @throws IOException
     */
    public void switchToAdmin(ActionEvent event) throws IOException { // REGISTRO
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminMainFunctions/AdminMain.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        controller.setUserData(uniqueId, userId, userPin);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Funcion que permite enviar informacion del usuario
     * @param event
     * @throws IOException
     * @throws MessagingException
     */
    public void enviarInformacion(ActionEvent event) throws IOException, MessagingException { // REGISTRO
        DatabaseManager databaseManager = new DatabaseManager(); // Se instancia el manager de base de datos
        if(databaseManager.existeInspector(inspectorCargado.getText())) { // Se revisa si existe el inspector
            inspectorCargado.setDisable(true);
            infoInspector.setText("Inspector Ingresado Existe");

            Random random = new Random();
            int validacion = random.nextInt(900) + 100; // Se le asigna un numero random al inspector
            String validacionString = Integer.toString(validacion);
            databaseManager.actualizarCodigoCambioInspector(inspectorCargado.getText(), validacion); // Se carga este numero random a la db

            // Ahora deberia de mandar el correo
            String correoUsuario = databaseManager.obtenerCorreoAdministrador(userId);

            String message =
                    "<p>La aplicacion de correos callejeros le envia saludos, " + userId + ",</p>" +
                            "<p>Este correo se le envia debido a que usted acaba de solicitar un cambio de contraseña a un inspector.</p>" +
                            "<p>El nombre del inspector es: <strong>" + inspectorCargado.getText() + "</strong></p>" +
                            "<p>El codigo de validacion para cambiar la contraseña es: <strong>" + validacionString + "</strong></p>" +
                            "<p>Este inspector ya puede hacer uso de la aplicacion.</p>" +
                            "<p>Muchas gracias por confiar en nosotros.</p>" +
                            "<p>Atentamente,<br>Parqueos Callejeros S.A</p>";

            EnvioCorreos envioCorreos = new EnvioCorreos(); // Se manda sta informacion al usuario
            envioCorreos.createEmail(correoUsuario, "Cambio de contraseña Inspector", message);
            envioCorreos.sendEmail();

            codigoValidacion.setDisable(false);
            nuevoPin.setDisable(false);



        } else {
            infoInspector.setText("Inspector No Existe");
        }
    }


    /**
     * Informacion permite mandar pin
     * @param event
     * @throws IOException
     * @throws MessagingException
     */
    public void cargarPin(ActionEvent event) throws IOException, MessagingException {
        String nuevoPinText = nuevoPin.getText(); // Se obtiene la informacion del pin
        DatabaseManager databaseManager = new DatabaseManager(); // Se instancia el manejador de la base de datos
        // Validar que el nuevo PIN sea numérico y tenga exactamente 4 dígitos
        if (nuevoPinText.matches("\\d{4}")) {
            // Si pasa la validación, proceder con el registro o la lógica que quieras
            infoSend.setText("El PIN es válido.");
            if(codigoValidacion.getText().matches("\\d{3}")){
                Integer validacionInt = Integer.parseInt(codigoValidacion.getText()); // Se revisa que el pin dado sea el mismo de la db
                if(databaseManager.cambiarPinInspector(inspectorCargado.getText(), validacionInt, nuevoPinText)){
                    infoSend.setText("Se cambio el pin con exito");
                    // Aqui se tiene que mandar un correo de verificacion
                    databaseManager.actualizarCodigoCambioInspector(inspectorCargado.getText(), 0);

                    String correoUsuario = databaseManager.obtenerCorreoInspector(inspectorCargado.getText());

                    String message =
                            "<p>La aplicacion de correos callejeros le envia saludos, " + inspectorCargado.getText() + ",</p>" +
                                    "<p>Este correo se le envia debido a que se cambio la contraseña de su cuenta de inspector.</p>" +
                                    "<p>Su nombre del inspector es: <strong>" + inspectorCargado.getText() + "</strong></p>" +
                                    "<p>Y su nueva contraseña es: <strong>" + nuevoPinText + "</strong></p>" +
                                    "<p>Esta contraseña ya puede ser utilizada en la aplicacion.</p>" +
                                    "<p>Muchas gracias por confiar en nosotros.</p>" +
                                    "<p>Atentamente,<br>Parqueos Callejeros S.A</p>";

                    EnvioCorreos envioCorreos = new EnvioCorreos();
                    envioCorreos.createEmail(correoUsuario, "Cambio de contraseña Inspector", message);
                    envioCorreos.sendEmail(); // Se envia un correo confirmando la accion

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Admin/AdminMainFunctions/AdminMain.fxml"));
                    Parent root = loader.load();
                    MainController controller = loader.getController();
                    controller.setUserData(uniqueId, userId, userPin);
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                } else {
                    infoSend.setText("El codigo de validacion ingresado es incorrecto");
                }
            } else {
                infoSend.setText("El codigo de validacion debe de ser un numero de tres digitos");
            }
        } else {
            // Si no es válido, mostrar un mensaje de error o manejar la excepción
            infoSend.setText("El PIN debe ser un número de 4 dígitos.");
        }
    }

}
