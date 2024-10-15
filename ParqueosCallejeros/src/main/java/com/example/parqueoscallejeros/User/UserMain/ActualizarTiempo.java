package com.example.parqueoscallejeros.User.UserMain;

import com.example.parqueoscallejeros.EnvioCorreos;
import com.example.parqueoscallejeros.dataBase.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ActualizarTiempo {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private int uniqueId;
    private String userId;
    private String userPin;

    @FXML
    private ComboBox<String> carrosParqueadosAccordion;

    @FXML
    private Label costoMinimoLabel;

    @FXML
    private TextField tiempoAdicional;

    @FXML
    private Label infoInput;

    public void setUserData(int id, String userId, String userPin) {
        System.out.println("PARQUEAR CONTROLLER");
        System.out.println(id);
        System.out.println(userId);
        System.out.println(userPin);
        this.uniqueId = id;
        this.userId = userId;
        this.userPin = userPin;
        cargarPlacasUsadas();
        //cargarEspaciosDisponibles();
        //tiempoMinimoIngresado.setDisable(true);
        //espacioParqueo.setDisable(true);
    }

    private void cargarPlacasUsadas() {
        List<String> placas = obtenerPlacasUsadasPorUsuario(uniqueId);
        carrosParqueadosAccordion.getItems().clear();

        DatabaseManager databaseManager = new DatabaseManager(); // Mueve esto fuera del bucle para crear solo una instancia

        for (String placa : placas) {
            Integer idParqueo = databaseManager.obtenerIdEspacioPorPlaca(placa);
            String horaParqueo = databaseManager.obtenerFechaReserva(idParqueo);
            Integer tiempoReservado = databaseManager.obtenerTiempoReservado(idParqueo);

            // Convertir la cadena de fecha a LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime fechaParqueo = LocalDateTime.parse(horaParqueo, formatter);

            // Obtener la fecha y hora actual
            LocalDateTime fechaActual = LocalDateTime.now();

            // Calcular la diferencia en minutos
            long minutosDiferencia = ChronoUnit.MINUTES.between(fechaParqueo, fechaActual);

            // Verificar si el tiempo no ha pasado
            if (minutosDiferencia < tiempoReservado) {
                System.out.println("Placa: " + placa + " está dentro del tiempo reservado. Tiempo transcurrido: " + minutosDiferencia + " minutos.");
                // Agregar solo placas que no están pasadas de tiempo
                carrosParqueadosAccordion.getItems().add(placa);
            } else {
                System.out.println("Placa: " + placa + " ha pasado de tiempo. Tiempo transcurrido: " + minutosDiferencia + " minutos.");
            }
        }

        // Seleccionar la primera placa cargada si hay alguna
        if (!carrosParqueadosAccordion.getItems().isEmpty()) {
            carrosParqueadosAccordion.getSelectionModel().select(0);
        }
    }


    private List<String> obtenerPlacasUsadasPorUsuario(int id){
        DatabaseManager databaseManager = new DatabaseManager();
        return databaseManager.obtenerPlacasOcupadasPorUsuario(id);
    }


    public void switchToMain(ActionEvent event) throws IOException { // REGISTRO
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/User/UserMainFunctions/UsuarioMain.fxml"));
        Parent root = loader.load();

        // Obtén el controlador del nuevo FXML
        MainController controller = loader.getController();

        // Pasa el valor (por ejemplo, el idUsuario)
        controller.setUserData(this.uniqueId, this.userId, this.userPin);

        // Cambia la escena
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void tramitarPlaca(ActionEvent event) throws IOException {
        DatabaseManager databaseManager = new DatabaseManager();
        Integer idParqueo = databaseManager.obtenerIdEspacioPorPlaca(carrosParqueadosAccordion.getSelectionModel().getSelectedItem());

        Integer costoMinimo = databaseManager.obtenerTiempoMinimo(idParqueo);
        System.out.println(costoMinimo);
        String costoMinimoString = Integer.toString(costoMinimo);
        costoMinimoLabel.setText("El costo mínimo que se puede tramitar es: " + costoMinimoString);

        String tiempoAdicionalText = tiempoAdicional.getText(); // Suponiendo que este es el campo de entrada
        try {
            Integer tiempoAdicionalInt = Integer.parseInt(tiempoAdicionalText); // Intentar convertir a entero

            // Verificar que el tiempoAdicional es un múltiplo de costoMinimo
            if (tiempoAdicionalInt > 0 && tiempoAdicionalInt % costoMinimo == 0) {
                // Aquí puedes continuar con la lógica si la verificación es exitosa
                infoInput.setText("El tiempo adicional es válido y es un múltiplo de costo mínimo.");
                String tiempoAdicionalString = Integer.toString(tiempoAdicionalInt);
                Integer costoPorHora = databaseManager.obtenerPrecioPorHora(idParqueo);
                System.out.println(costoPorHora);
                Integer precioFinal = (int) ((double) costoPorHora / 60 * tiempoAdicionalInt);
                String precioFinalString = Integer.toString(precioFinal);
                databaseManager.aumentarTiempoYCostoPorPlaca(carrosParqueadosAccordion.getSelectionModel().getSelectedItem(), tiempoAdicionalInt, precioFinal);
                // Añadir más lógica aquí...

                String correoUsuario = databaseManager.obtenerCorreoUsuario(userId);

                String message =
                        "<p>La aplicacion de correos callejeros le envia saludos, " + userId + ",</p>" +
                                "<p>Este correo se le envia debido a que usted acaba de aumentar el tiempo de parqueo en la aplicacion.</p>" +
                                "<p>El tiempo agregado es: <strong>" + tiempoAdicionalString + "</strong></p>" +
                                "<p>EL precio final de este tiempo es: <strong>" + precioFinalString + "</strong></p>" +
                                "<p>Muchas gracias por confiar en nosotros.</p>" +
                                "<p>Atentamente,<br>Parqueos Callejeros S.A</p>";

                EnvioCorreos envioCorreos = new EnvioCorreos();
                envioCorreos.createEmail(correoUsuario, "Se actualizo tiempo", message);
                envioCorreos.sendEmail();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/User/UserMainFunctions/UsuarioMain.fxml"));
                Parent root = loader.load();

                // Obtén el controlador del nuevo FXML
                MainController controller = loader.getController();

                // Pasa el valor (por ejemplo, el idUsuario)
                controller.setUserData(this.uniqueId, this.userId, this.userPin);

                // Cambia la escena
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                // Mostrar un mensaje de error si no es múltiplo o no es positivo
                infoInput.setText("El tiempo adicional debe ser un número positivo y un múltiplo de " + costoMinimo + ".");
                // Podrías mostrar un mensaje en una etiqueta o un cuadro de diálogo
            }
        } catch (NumberFormatException e) {
            // Manejar la excepción si la conversión falla
            infoInput.setText("Por favor, ingrese un número válido para el tiempo adicional.");
            // Mostrar un mensaje de error en una etiqueta o un cuadro de diálogo
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
