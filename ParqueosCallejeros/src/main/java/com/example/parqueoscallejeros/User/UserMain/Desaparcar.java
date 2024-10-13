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

    private void cargarPlacasUsadas(){
        List<String> placas = obtenerPlacasUsadasPorUsuario(uniqueId);
        carrosParqueadosAccordion.getItems().clear();

        for(String placa : placas){
            carrosParqueadosAccordion.getItems().add(placa);
        }
        carrosParqueadosAccordion.getSelectionModel().select(0);

    }

    private List<String> obtenerPlacasUsadasPorUsuario(int id){
        DatabaseManager databaseManager = new DatabaseManager();
        return databaseManager.obtenerPlacasOcupadasPorUsuario(id);
    }


    public void desaparcarAction(ActionEvent event) throws IOException {
        DatabaseManager databaseManager = new DatabaseManager();
        String placaSeleccionada = carrosParqueadosAccordion.getSelectionModel().getSelectedItem();
        Integer idParqueo = databaseManager.obtenerIdEspacioPorPlaca(placaSeleccionada);
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

        // Verificar si la diferencia es menor que tiempoReservado
        if (minutosDiferencia < tiempoReservado) {
            System.out.println(minutosDiferencia); // Imprimir los minutos reservados
            System.out.println("Tiempo sobrante: " + tiempoSobrante + " minutos"); // Imprimir el tiempo sobrante

            // ACCION DE IR BORRANDO LAS COSITAS
            databaseManager.actualizarEstadoVehiculoDesparqueado(placaSeleccionada);
            databaseManager.actualizarEstadoEspacioDesparqueado(idParqueo);
            databaseManager.actualizarTiempoOcupado(uniqueId, (int) tiempoSobrante);
            databaseManager.eliminarReservaPorEspacio(idParqueo);
            databaseManager.sumarAcumuladosPorUsuario(uniqueId, (int) tiempoSobrante);

        } else {
            databaseManager.actualizarEstadoVehiculoDesparqueado(placaSeleccionada);
            databaseManager.actualizarEstadoEspacioDesparqueado(idParqueo);
            databaseManager.actualizarTiempoOcupado(uniqueId, (int) tiempoSobrante);
            databaseManager.eliminarReservaPorEspacio(idParqueo);
            //databaseManager.actualizarAcumuladosPorUsuario(uniqueId, (int) tiempoSobrante); // Imprimir false
        }
    }
}