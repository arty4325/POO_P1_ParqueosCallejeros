package com.example.parqueoscallejeros.Reportes;

import com.itextpdf.text.DocumentException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class generadorPdf {
    @FXML
    private DatePicker fechaInicioPicker;

    @FXML
    private DatePicker fechaFinPicker;

    @FXML
    public void multasPdf(ActionEvent actionEvent) throws DocumentException, FileNotFoundException {
        LocalDate fechaInicio = fechaInicioPicker.getValue(); // Este es LocalDate
        LocalDate fechaFin = fechaFinPicker.getValue(); // Este es LocalDate

        if (fechaInicio == null || fechaFin == null) {
            // Manejo de error: podrías mostrar un mensaje al usuario
            System.out.println("Fechas no seleccionadas.");
            return;
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }

        // Crear LocalDateTime para el inicio y el fin del rango
        LocalDateTime fechaInicioLDT = LocalDateTime.of(fechaInicio, LocalTime.MIN); // 00:00:00
        LocalDateTime fechaFinLDT = LocalDateTime.of(fechaFin, LocalTime.MAX); // 23:59:59.999999999


        Reportes reporte = new Reportes();
        reporte.crearDocumento("HistorialMultas");
        reporte.abrirDocumento();
        reporte.agregarTitulo("REPORTE DE DINERO GANADO POR MULTAS");
        reporte.agregarSaltosDeLinea();
        reporte.agregarTexto("Reporte actual de multas " + fechaInicio +" "+ fechaFin);
        reporte.agregarSaltosDeLinea();
        reporte.agregarTablaMultas(fechaInicioLDT, fechaFinLDT);
        reporte.cerrarDocumento();
        System.out.println("Reporte finalizado.");
    }
    public void dineroMultasPdf(ActionEvent actionEvent) throws DocumentException, FileNotFoundException {
        LocalDate fechaInicio = fechaInicioPicker.getValue(); // Este es LocalDate
        LocalDate fechaFin = fechaFinPicker.getValue(); // Este es LocalDate

        if (fechaInicio == null || fechaFin == null) {
            // Manejo de error: podrías mostrar un mensaje al usuario
            System.out.println("Fechas no seleccionadas.");
            return;
        }
        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }

        // Crear LocalDateTime para el inicio y el fin del rango
        LocalDateTime fechaInicioLDT = LocalDateTime.of(fechaInicio, LocalTime.MIN); // 00:00:00
        LocalDateTime fechaFinLDT = LocalDateTime.of(fechaFin, LocalTime.MAX); // 23:59:59.999999999


        Reportes reporte = new Reportes();
        reporte.crearDocumento("DineroMultas");
        reporte.abrirDocumento();
        reporte.agregarTitulo("REPORTE DE MULTAS");
        reporte.agregarSaltosDeLinea();
        reporte.agregarTexto("Reporte actual de multas " + fechaInicio +" "+ fechaFin);
        reporte.agregarSaltosDeLinea();
        reporte.agregarTablaDineroMultas(fechaInicioLDT, fechaFinLDT);
        reporte.cerrarDocumento();
        System.out.println("Reporte finalizado.");
    }
}
