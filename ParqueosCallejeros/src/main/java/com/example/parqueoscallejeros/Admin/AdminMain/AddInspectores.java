package com.example.parqueoscallejeros.Admin.AdminMain;

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

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class AddInspectores {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private int uniqueId;
    private String userId;
    private String userPin;
    @FXML
    private TextField varNombre;

    @FXML
    private TextField varApellidos;

    @FXML
    private TextField varTelefono;

    @FXML
    private TextField varCorreo;

    @FXML
    private TextField varDireccion;

    @FXML
    private TextField varIdentificacion;

    @FXML
    private TextField varPin;

    @FXML
    private TextField varTerminal;

    @FXML
    private Label messageInfo;

    public void setUserData(int id, String userId, String userPin) {
        System.out.println(id);
        System.out.println(userId);
        System.out.println(userPin);
        this.uniqueId = id;
        this.userId = userId;
        this.userPin = userPin;
    }



    public void validarDatosInspector() throws MessagingException {
        // Nombre: string de 2 a 20 caracteres
        String nombre = varNombre.getText();
        if (nombre.length() < 2 || nombre.length() > 20) {
            messageInfo.setText("Error: El nombre debe tener entre 2 y 20 caracteres.");
            return;
        }

        // Apellidos: string de 1 a 40 caracteres
        String apellidos = varApellidos.getText();
        if (apellidos.length() < 1 || apellidos.length() > 40) {
            messageInfo.setText("Error: Los apellidos deben tener entre 1 y 40 caracteres.");
            return;
        }

        // Teléfono: número natural de 8 dígitos exactos
        String telefono = varTelefono.getText();
        if (!telefono.matches("\\d{8}")) {
            messageInfo.setText("Error: El teléfono debe ser un número de 8 dígitos.");
            return;
        }

        // Correo electrónico: string con formato parte1@parte2
        String correo = varCorreo.getText();
        String regexCorreo = "^[\\w.-]{3,}@[\\w.-]{3,}$";
        if (!Pattern.matches(regexCorreo, correo)) {
            messageInfo.setText("Error: El correo electrónico no tiene un formato válido.");
            return;
        }

        // Dirección: string de 5 a 60 caracteres
        String direccion = varDireccion.getText();
        if (direccion.length() < 5 || direccion.length() > 60) {
            messageInfo.setText("Error: La dirección debe tener entre 5 y 60 caracteres.");
            return;
        }

        // Identificación de usuario: string de 2 a 25 caracteres
        String identificacion = varIdentificacion.getText();
        if (identificacion.length() < 2 || identificacion.length() > 25) {
            messageInfo.setText("Error: La identificación debe tener entre 2 y 25 caracteres.");
            return;
        }

        // PIN: string de 4 caracteres exactos
        String pin = varPin.getText();
        if (!pin.matches("\\d{4}")) {
            messageInfo.setText("Error: El PIN debe ser un número de 4 dígitos.");
            return;
        }

        // Terminal: código string de 6 caracteres exactos
        String terminal = varTerminal.getText();
        if (terminal.length() != 6) {
            messageInfo.setText("Error: El código de la terminal debe tener exactamente 6 caracteres.");
            return;
        }



        // Si todas las validaciones pasaron:
        messageInfo.setText("Todos los datos son válidos. Inspector registrado correctamente.");
        LocalDate fechaIngreso = LocalDate.now();
        DatabaseManager databaseManager = new DatabaseManager();

        databaseManager.insertarInspector(nombre, apellidos, telefono, correo, direccion, fechaIngreso,
                identificacion, pin, terminal);

        String correoUsuario = databaseManager.obtenerCorreoUsuario(userId);

        String message =
                "<p>La aplicacion de correos callejeros le envia saludos, " + userId + ",</p>" +
                        "<p>Este correo se le envia debido a que usted acaba de agregar un Inspector al sistema.</p>" +
                        "<p>El nombre del inspector es: <strong>" + nombre + "</strong></p>" +
                        "<p>El apellido del inspector es: <strong>" + apellidos + "</strong></p>" +
                        "<p>Este inspector ya puede hacer uso de la aplicacion.</p>" +
                        "<p>Muchas gracias por confiar en nosotros.</p>" +
                        "<p>Atentamente,<br>Parqueos Callejeros S.A</p>";

        EnvioCorreos envioCorreos = new EnvioCorreos();
        envioCorreos.createEmail(correoUsuario, "Se agrego un Inspector", message);
        envioCorreos.sendEmail();
    }


    public void enviarInformacion(ActionEvent event) throws IOException, MessagingException { // REGISTRO
        validarDatosInspector();
    }
}
