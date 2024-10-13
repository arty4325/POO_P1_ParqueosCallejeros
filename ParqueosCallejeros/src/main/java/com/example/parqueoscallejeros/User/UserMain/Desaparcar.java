package com.example.parqueoscallejeros.User.UserMain;

import com.example.parqueoscallejeros.EnvioCorreos;
import com.example.parqueoscallejeros.dataBase.DatabaseManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Desaparcar {
    private Stage stage;
    private Scene scene;
    private Parent root;

    private int uniqueId;
    private String userId;
    private String userPin;

    @FXML
    private ComboBox<String> carrosParqueadosAccordion;

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
        List<String> placasParaEliminar = new ArrayList<>(); // Lista para almacenar placas a eliminar

        carrosParqueadosAccordion.getItems().clear();

        for (String placa : placas) {

            DatabaseManager databaseManager = new DatabaseManager();
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

            // Verificar si la diferencia es mayor o igual al tiempo reservado
            if (minutosDiferencia >= tiempoReservado) {
                // Ejecutar el bloque del else para cada placa
                // PRIMERO ACTUALIZO EL HISTORIAL
                Integer tiempo = databaseManager.obtenerTiempoReservado(idParqueo);
                Integer precio = databaseManager.obtenerPrecioPorHora(idParqueo);
                // Ahora calculo el precio
                Integer precioFinal = (int) ((double) precio / 60 * tiempo);
                // Ahora tengo que incertar la informacion en el historial
                String fechaReserva = databaseManager.obtenerFechaReserva(idParqueo);

                databaseManager.actualizarEstadoVehiculoDesparqueado(placa);
                databaseManager.actualizarEstadoEspacioDesparqueado(idParqueo);
                databaseManager.actualizarTiempoOcupado(uniqueId, tiempo, fechaReserva); // Asumiendo que es cero
                // Ahora tengo que subir lo que gaste
                databaseManager.actualizarCostoEnTablaCosto(uniqueId, precioFinal, fechaReserva);
                databaseManager.eliminarReservaPorEspacio(idParqueo);

                // Agregar la placa a la lista de placas para eliminar
                placasParaEliminar.add(placa);
            } else {
                // Si no se cumple la condición, la placa se añade al Accordion
                carrosParqueadosAccordion.getItems().add(placa);
            }
        }

        // Eliminar las placas que fueron borradas del Accordion
        for (String placa : placasParaEliminar) {
            carrosParqueadosAccordion.getItems().remove(placa);
        }

        // Seleccionar el primer elemento si existe
        if (!carrosParqueadosAccordion.getItems().isEmpty()) {
            carrosParqueadosAccordion.getSelectionModel().select(0);
        }
    }


    private List<String> obtenerPlacasUsadasPorUsuario(int id){
        DatabaseManager databaseManager = new DatabaseManager();
        return databaseManager.obtenerPlacasOcupadasPorUsuario(id);
    }


    public void desaparcarAction(ActionEvent event) throws IOException, MessagingException {
        DatabaseManager databaseManager = new DatabaseManager();
        String placaSeleccionada = carrosParqueadosAccordion.getSelectionModel().getSelectedItem();
        Integer idParqueo = databaseManager.obtenerIdEspacioPorPlaca(placaSeleccionada);
        String idParqueoString = Integer.toString(idParqueo);
        String horaParqueo = databaseManager.obtenerFechaReserva(idParqueo);
        Integer tiempoReservado = databaseManager.obtenerTiempoReservado(idParqueo);


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

        // Verificar si la diferencia es menor que tiempoReservado
        if (minutosDiferencia < tiempoReservado) {
            System.out.println(minutosDiferencia); // Imprimir los minutos reservados
            System.out.println("Tiempo sobrante: " + tiempoSobrante + " minutos"); // Imprimir el tiempo sobrante
            String fechaReserva = databaseManager.obtenerFechaReserva(idParqueo);

            Integer precio = databaseManager.obtenerPrecioPorHora(idParqueo);
            // Ahora calculo el precio
            Integer precioFinal = (int) ((double) precio / 60 * minutosDiferencia);
            // ACCION DE IR BORRANDO LAS COSITAS
            databaseManager.actualizarEstadoVehiculoDesparqueado(placaSeleccionada);
            databaseManager.actualizarEstadoEspacioDesparqueado(idParqueo);
            databaseManager.actualizarTiempoOcupado(uniqueId, (int) minutosDiferencia, fechaReserva);
            databaseManager.actualizarCostoEnTablaCosto(uniqueId, precioFinal, fechaReserva);
            databaseManager.eliminarReservaPorEspacio(idParqueo);
            databaseManager.sumarAcumuladosPorUsuario(uniqueId, (int) tiempoSobrante);

            String correoUsuario = databaseManager.obtenerCorreoUsuario(userId);

            String message =
                    "<p>La aplicacion de correos callejeros le envia saludos, " + userId + ",</p>" +
                            "<p>Este correo incluye la informacion del carro que suted acaba de desaparcar.</p>" +
                            "<p>El espacio donde usted acaba de desaparcar es: <strong>" + idParqueoString + "</strong></p>" +
                            "<p>La placa del vehiculo que usted acaba de parquear es: <strong>" + placaSeleccionada + "</strong></p>" +
                            "<p>El tiempo que usted estuvo aparcado fue: <strong>" + minutosDiferenciaString + "</strong></p>" +
                            "<p>Muchas gracias por confiar en nosotros.</p>" +
                            "<p>Atentamente,<br>Parqueos Callejeros S.A</p>";

            EnvioCorreos envioCorreos = new EnvioCorreos();
            envioCorreos.createEmail(correoUsuario, "Informacion Carro Desaparcado", message);
            envioCorreos.sendEmail();

        }
    }


}
