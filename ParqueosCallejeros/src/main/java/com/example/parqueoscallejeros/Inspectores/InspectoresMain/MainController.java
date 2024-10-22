package com.example.parqueoscallejeros.Inspectores.InspectoresMain;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class MainController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private int uniqueId;
    private String userId;
    private String userPin;

    @FXML
    private TextField numParqueo;

    @FXML
    private TextField idPlaca;

    @FXML
    private Label feedbackData;

    @FXML
    private Label feedbackData1;

    /**
     * Funcion que permite configurar la informacion del usuario segun lo que habia en otra ventana
     * @param id
     * @param userId
     * @param userPin
     */
    public void setUserData(int id, String userId, String userPin) {
        System.out.println(id);
        System.out.println(userId);
        System.out.println(userPin);
        this.uniqueId = id;
        this.userId = userId;
        this.userPin = userPin;
    }

    // Cambia a la ventana de reportes
    public void switchReportes(ActionEvent event) throws IOException { // CAMBIAR A REPORTES
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/parqueoscallejeros/Inspectores/InspectoresMain/InspectorReport.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Envia la informacion del paruqeo que se esta verificando
    public void sendData(ActionEvent event) throws IOException, MessagingException {
        DatabaseManager databaseManager = new DatabaseManager();
        String numParqueoText = numParqueo.getText();
        String idPlacaText = idPlaca.getText();
        Integer numParqueoInt = Integer.parseInt(numParqueoText);

        // Validar numParqueo
        if (!numParqueoText.matches("\\d{4}")) {
            feedbackData.setText("El número de parqueo debe contener exactamente 4 dígitos numéricos.");
            return; // Salir del método si la validación falla
        }

        // Validar idPlaca
        if (!idPlacaText.matches("^[a-zA-Z0-9]{1,6}$")) {
            feedbackData.setText("El ID de la placa debe contener entre 1 y 6 dígitos numéricos.");
            return; // Salir del método si la validación falla
        }

        // Aquí continúa la lógica para enviar los datos si ambas validaciones son exitosas

        if(databaseManager.verificarConfiguracionParqueoExistente(numParqueoInt)) { // Se verifica si el parqueo ingresado esta en la db
            if (databaseManager.existeReservaPorEspacioYPlaca(numParqueoInt, idPlacaText)) {
                System.out.println("SI ESTA PARQUEADO ");
                String horaParqueo = databaseManager.obtenerFechaReserva(numParqueoInt);
                Integer tiempoReservado = databaseManager.obtenerTiempoReservado(numParqueoInt);


                // Convertir la cadena de fecha a LocalDateTime
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime fechaParqueo = LocalDateTime.parse(horaParqueo, formatter);

                // Obtener la fecha y hora actual
                LocalDateTime fechaActual = LocalDateTime.now();

                // Calcular la diferencia en minutos
                long minutosDiferencia = ChronoUnit.MINUTES.between(fechaParqueo, fechaActual);

                // Calcular el tiempo sobrante
                long tiempoSobrante = tiempoReservado - minutosDiferencia;

                String minutosDiferenciaString = Long.toString(minutosDiferencia);
                System.out.println(tiempoSobrante);
                if (tiempoSobrante > 0) {
                    feedbackData1.setText("El carro esta parqueado");
                } else {
                    feedbackData1.setText("El carro no está parqueado");
                    Integer costo;
                    costo = databaseManager.obtenerCostoMulta(numParqueoInt);

                    // Obtén la fecha actual
                    LocalDateTime ahora = LocalDateTime.now();
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String fechaFormateada = ahora.format(format);
                    String correo = databaseManager.obtenerCorreoPorPlaca(idPlacaText);
                    if(!(correo == null)) {
                        String costoString = Integer.toString(costo);
                        String message =
                                "<p>La aplicacion de correos callejeros le envia saludos,</p>" +
                                        "<p>Este correo se le envia debido a que se tramito una multa a su carro.</p>" +
                                        "<p>La placa del carro es: <strong>" + idPlacaText + "</strong></p>" +
                                        "<p>Y el precio es: <strong>" + costoString + "</strong></p>" +
                                        "<p>Esta contraseña ya puede ser utilizada en la aplicacion.</p>" +
                                        "<p>Muchas gracias por confiar en nosotros.</p>" +
                                        "<p>Atentamente,<br>Parqueos Callejeros S.A</p>";

                        EnvioCorreos envioCorreos = new EnvioCorreos(); // Se envia un correo con la informacion
                        envioCorreos.createEmail(correo, "Generacion de Multa", message);
                        envioCorreos.sendEmail();
                        // Llama al método insertarMulta pasando el LocalDate como último parámetro
                    }
                    databaseManager.insertarMulta(uniqueId, idPlacaText, costo, fechaFormateada);
                }
            } else { // Se tiene que revisar si hay informacion del carro en la db
                feedbackData1.setText("El carro no está parqueado");
                Integer costo;
                costo = databaseManager.obtenerCostoMulta(numParqueoInt);

                // Obtén la fecha actual
                LocalDateTime ahora = LocalDateTime.now();
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String fechaFormateada = ahora.format(format);
                System.out.println(databaseManager.obtenerCorreoPorPlaca(idPlacaText));
                // Llama al método insertarMulta pasando el LocalDate como último parámetro
                String correo = databaseManager.obtenerCorreoPorPlaca(idPlacaText);
                if(!(correo == null)) {
                    String costoString = Integer.toString(costo);
                    String message =
                            "<p>La aplicacion de correos callejeros le envia saludos,</p>" +
                                    "<p>Este correo se le envia debido a que se tramito una multa a su carro.</p>" +
                                    "<p>La placa del carro es: <strong>" + idPlacaText + "</strong></p>" +
                                    "<p>Y el precio es: <strong>" + costoString + "</strong></p>" +
                                    "<p>Esta contraseña ya puede ser utilizada en la aplicacion.</p>" +
                                    "<p>Muchas gracias por confiar en nosotros.</p>" +
                                    "<p>Atentamente,<br>Parqueos Callejeros S.A</p>";

                    EnvioCorreos envioCorreos = new EnvioCorreos();
                    envioCorreos.createEmail(correo, "Generacion de Multa", message);
                    envioCorreos.sendEmail(); // Se envia un correocon la info
                }
                databaseManager.insertarMulta(uniqueId, idPlacaText, costo, fechaFormateada);
            }
            ;

            // YO QUIERO VER SI SE LE PASO EL TIEMPO
        } else {
            feedbackData.setText("El numero de parqueo no existe");
        }


    }

}
