package com.example.parqueoscallejeros.Reportes;

import com.itextpdf.text.DocumentException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

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
    private TextField regInicioList;

    @FXML
    private TextField regFinList;

    @FXML
    private LocalDateTime[] obtenerRangoDeFechas() {
        LocalDate fechaInicio = fechaInicioPicker.getValue(); // Obtener fecha de inicio
        LocalDate fechaFin = fechaFinPicker.getValue(); // Obtener fecha de fin

        if (fechaInicio == null || fechaFin == null) {
            // Manejo de error: lanzar excepción o mostrar mensaje de error
            System.out.println("Fechas no seleccionadas.");
            throw new IllegalArgumentException("Las fechas no pueden ser nulas.");
        }

        // Crear LocalDateTime para el inicio y el fin del rango
        LocalDateTime fechaInicioLDT = LocalDateTime.of(fechaInicio, LocalTime.MIN); // 00:00:00
        LocalDateTime fechaFinLDT = LocalDateTime.of(fechaFin, LocalTime.MAX); // 23:59:59.999999999

        return new LocalDateTime[] {fechaInicioLDT, fechaFinLDT}; // Retornar el rango de fechas
    }
    @FXML
    private int[] obtenerRangoDeParqueos() {
        String regInicio = regInicioList.getText();
        String regFin = regFinList.getText();
        if (regInicio == null || regFin == null) {
            System.out.println("Rango no seleccionado.");
            return null;
        }
        int inicioList = 0;
        int finList = 0;
        try {
            inicioList = Integer.parseInt(regInicio);
            finList = Integer.parseInt(regFin);
        } catch (NumberFormatException e) {
            System.out.println("Ingrese unicamente numeros");
            return null;
        }
        return new int[] {inicioList, finList};

    }

    /**
     * generador de las multas de usuario en pdf
     * @throws FileNotFoundException
     * @throws DocumentException
     */
    public void multasUserPdf() throws FileNotFoundException, DocumentException {
        int userId = Session.getInstance().getUserId();
        LocalDateTime fechaInicioLDT = LocalDateTime.now().minusYears(10); // Desde hace 10 años
        LocalDateTime fechaFinLDT = LocalDateTime.now(); // Hasta hoy
        Reportes reporte = new Reportes();
        reporte.crearDocumento("HistorialMultas");
        reporte.abrirDocumento();
        reporte.agregarTitulo("REPORTE DE DINERO GANADO POR MULTAS");
        reporte.agregarSaltosDeLinea();
        reporte.agregarTexto("Reporte actual de multas " + fechaInicioLDT +" "+ fechaFinLDT);
        reporte.agregarSaltosDeLinea();
        reporte.agregarTablaMultas(fechaInicioLDT, fechaFinLDT, userId);
        reporte.cerrarDocumento();
        System.out.println("Reporte finalizado.");
    }

    /**
     * generador de multas pdf
     * @param actionEvent
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void multasPdf(ActionEvent actionEvent) throws DocumentException, FileNotFoundException {
        try {
            // Llamar al método que obtiene el rango de fechas
            LocalDateTime[] rangoDeFechas = obtenerRangoDeFechas();
            LocalDateTime fechaInicioLDT = rangoDeFechas[0];
            LocalDateTime fechaFinLDT = rangoDeFechas[1];

            Reportes reporte = new Reportes();
            reporte.crearDocumento("HistorialMultas");
            reporte.abrirDocumento();
            reporte.agregarTitulo("REPORTE DE DINERO GANADO POR MULTAS");
            reporte.agregarSaltosDeLinea();
            reporte.agregarTexto("Reporte actual de multas " + fechaInicioLDT +" "+ fechaFinLDT);
            reporte.agregarSaltosDeLinea();
            reporte.agregarTablaMultas(fechaInicioLDT, fechaFinLDT, 0);
            reporte.cerrarDocumento();
            System.out.println("Reporte finalizado.");
        } catch (IllegalArgumentException e) {
            // Manejo de excepción si las fechas no fueron seleccionadas
            System.out.println(e.getMessage());
        }
    }

    /**
     * Dinero multas pdf
     * @param actionEvent
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void dineroMultasPdf(ActionEvent actionEvent) throws DocumentException, FileNotFoundException {
        try {
            // Llamar al método que obtiene el rango de fechas
            LocalDateTime[] rangoDeFechas = obtenerRangoDeFechas();
            LocalDateTime fechaInicioLDT = rangoDeFechas[0];
            LocalDateTime fechaFinLDT = rangoDeFechas[1];
            Reportes reporte = new Reportes();
            reporte.crearDocumento("DineroMultas");
            reporte.abrirDocumento();
            reporte.agregarTitulo("REPORTE DE MULTAS");
            reporte.agregarSaltosDeLinea();
            reporte.agregarTexto("Reporte actual de multas " + fechaInicioLDT +" "+ fechaFinLDT);
            reporte.agregarSaltosDeLinea();
            reporte.agregarTablaDineroMultas(fechaInicioLDT, fechaFinLDT);
            reporte.cerrarDocumento();
            System.out.println("Reporte finalizado.");
        } catch (IllegalArgumentException e) {
            // Manejo de excepción si las fechas no fueron seleccionadas
            System.out.println(e.getMessage());
        }
    }

    /**
     * genera el pdf del historial de un usuario
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void historialUsoUserPdf() throws DocumentException, FileNotFoundException {
        int userId = Session.getInstance().getUserId();
        LocalDateTime fechaInicioLDT = LocalDateTime.now().minusYears(10); // Desde hace 10 años
        LocalDateTime fechaFinLDT = LocalDateTime.now(); // Hasta hoy
        Reportes reporte = new Reportes();
        reporte.crearDocumento("HistorialUso");
        reporte.abrirDocumento();
        reporte.agregarTitulo("REPORTE DE USO");
        reporte.agregarSaltosDeLinea();
        reporte.agregarTexto("Reporte actual de uso " + fechaInicioLDT +" "+ fechaFinLDT);
        reporte.agregarSaltosDeLinea();
        reporte.agregarTablaHistorialUso(fechaInicioLDT, fechaFinLDT, userId);
        reporte.cerrarDocumento();
        System.out.println("Reporte finalizado.");
    }

    /**
     * genera el pdf dle historial de uso
     * @param actionEvent
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void historialUsoPdf(ActionEvent actionEvent) throws DocumentException, FileNotFoundException {
        try {
            // Llamar al método que obtiene el rango de fechas
            LocalDateTime[] rangoDeFechas = obtenerRangoDeFechas();
            LocalDateTime fechaInicioLDT = rangoDeFechas[0];
            LocalDateTime fechaFinLDT = rangoDeFechas[1];
            Reportes reporte = new Reportes();
            reporte.crearDocumento("HistorialUso");
            reporte.abrirDocumento();
            reporte.agregarTitulo("REPORTE DE USO");
            reporte.agregarSaltosDeLinea();
            reporte.agregarTexto("Reporte actual de uso " + fechaInicioLDT +" "+ fechaFinLDT);
            reporte.agregarSaltosDeLinea();
            reporte.agregarTablaHistorialUso(fechaInicioLDT, fechaFinLDT, 0);
            reporte.cerrarDocumento();
            System.out.println("Reporte finalizado.");
        } catch (IllegalArgumentException e) {
            // Manejo de excepción si las fechas no fueron seleccionadas
            System.out.println(e.getMessage());
        }
    }


    /**
     * genera un pdf del ingreso de dinero en un tiempo dado
     * @param actionEvent
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void IngresoDineroPdf(ActionEvent actionEvent) throws DocumentException, FileNotFoundException {
        try {
            // Llamar al método que obtiene el rango de fechas
            LocalDateTime[] rangoDeFechas = obtenerRangoDeFechas();
            LocalDateTime fechaInicioLDT = rangoDeFechas[0];
            LocalDateTime fechaFinLDT = rangoDeFechas[1];
            Reportes reporte = new Reportes();
            reporte.crearDocumento("Ingreso de Dinero");
            reporte.abrirDocumento();
            reporte.agregarTitulo("REPORTE DE DINERO");
            reporte.agregarSaltosDeLinea();
            reporte.agregarTexto("Reporte actual de uso " + fechaInicioLDT +" "+ fechaFinLDT);
            reporte.agregarSaltosDeLinea();
            reporte.agregarTablaIngresoDinero(fechaInicioLDT, fechaFinLDT, true);
            reporte.cerrarDocumento();
            System.out.println("Reporte finalizado.");
        } catch (IllegalArgumentException e) {
            // Manejo de excepción si las fechas no fueron seleccionadas
            System.out.println(e.getMessage());
        }
    }

    /**
     * genera un pdf de todos los espacios de paruqeos
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void EspacioParqueoTodos() throws DocumentException, FileNotFoundException {
        int estado=0;
        Reportes reporte = new Reportes();
        reporte.crearDocumento("Espacios Parqueo");
        reporte.abrirDocumento();
        reporte.agregarTitulo("REPORTE DE ESPACIOS");
        reporte.agregarSaltosDeLinea();
        reporte.agregarSaltosDeLinea();
        LocalDateTime fechaInicioLDT = LocalDateTime.now().minusYears(10); // Desde hace 10 años
        LocalDateTime fechaFinLDT = LocalDateTime.now(); // Hasta hoy
        reporte.agregarTitulo("REPORTE DE ESPACIOS OCUPADOS");
        reporte.agregarTablaIngresoDinero(fechaInicioLDT, fechaFinLDT, false);
        reporte.agregarTitulo("REPORTE DE ESPACIOS VACIOS");
        reporte.agregarTablaEspaciosParqueo(estado);
        reporte.cerrarDocumento();
        System.out.println("Reporte finalizado.");

    }

    /**
     * genera un historial para todos los paruqeos que estan ocupados
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void EspacioParqueoOcupados() throws DocumentException, FileNotFoundException {
        int estado=0;
        Reportes reporte = new Reportes();
        reporte.crearDocumento("Espacios Parqueo");
        reporte.abrirDocumento();
        reporte.agregarTitulo("REPORTE DE ESPACIOS OCUPADOS");
        reporte.agregarSaltosDeLinea();
        reporte.agregarSaltosDeLinea();
        LocalDateTime fechaInicioLDT = LocalDateTime.now().minusYears(10); // Desde hace 10 años
        LocalDateTime fechaFinLDT = LocalDateTime.now(); // Hasta hoy
        reporte.agregarTablaIngresoDinero(fechaInicioLDT, fechaFinLDT, false);
        reporte.cerrarDocumento();
        System.out.println("Reporte finalizado.");
    }

    /**
     * genera reporte de los espacios vacios en pdf
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void EspacioParqueoVacios() throws DocumentException, FileNotFoundException {
        int estado=0;
        Reportes reporte = new Reportes();
        reporte.crearDocumento("Espacios Parqueo");
        reporte.abrirDocumento();
        reporte.agregarTitulo("REPORTE DE ESPACIOS VACIOS");
        reporte.agregarSaltosDeLinea();
        reporte.agregarSaltosDeLinea();
        reporte.agregarTablaEspaciosParqueo(estado);
        reporte.cerrarDocumento();
        System.out.println("Reporte finalizado.");
    }

    /**
     * reporte detallado de lo ocurrido en los paruqos
     * @param actionEvent
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void reporteDetalladoPdf(ActionEvent actionEvent) throws DocumentException, FileNotFoundException {
        try {
            // Llamar al método que obtiene el rango de fechas
            LocalDateTime[] rangoDeFechas = obtenerRangoDeFechas();
            LocalDateTime fechaInicioLDT = rangoDeFechas[0];
            LocalDateTime fechaFinLDT = rangoDeFechas[1];
            int[] rangoEspacios = obtenerRangoDeParqueos();
            int inicioList = rangoEspacios[0];
            int finList = rangoEspacios[1];
            Reportes reporte = new Reportes();
            reporte.crearDocumento("Estaditica Detallada");
            reporte.abrirDocumento();
            reporte.agregarTitulo("REPORTE DETALLADO");
            reporte.agregarSaltosDeLinea();
            reporte.agregarTexto("Reporte actual de uso " + fechaInicioLDT +" "+ fechaFinLDT);
            reporte.agregarSaltosDeLinea();
            reporte.agregarTablaEstDetallada(fechaInicioLDT, fechaFinLDT, inicioList, finList);
            reporte.cerrarDocumento();
            System.out.println("Reporte finalizado.");
        } catch (IllegalArgumentException e) {
            // Manejo de excepción si las fechas no fueron seleccionadas
            System.out.println(e.getMessage());
        }
    }

    /**
     * gener aun reporte resumido en un pdf
     * @param actionEvent
     * @throws DocumentException
     * @throws FileNotFoundException
     */
    public void reporteResumidoPdf(ActionEvent actionEvent) throws DocumentException, FileNotFoundException {
        try {
            // Llamar al método que obtiene el rango de fechas
            LocalDateTime[] rangoDeFechas = obtenerRangoDeFechas();
            LocalDateTime fechaInicioLDT = rangoDeFechas[0];
            LocalDateTime fechaFinLDT = rangoDeFechas[1];
            int[] rangoEspacios = obtenerRangoDeParqueos();
            int inicioList = rangoEspacios[0];
            int finList = rangoEspacios[1];
            Reportes reporte = new Reportes();
            reporte.crearDocumento("Estadistica resumida");
            reporte.abrirDocumento();
            reporte.agregarTitulo("REPORTE RESUMIDO");
            reporte.agregarSaltosDeLinea();
            reporte.agregarTexto("Reporte actual de uso " + fechaInicioLDT +" "+ fechaFinLDT);
            reporte.agregarSaltosDeLinea();
            reporte.agregarTablaEstResumida(fechaInicioLDT, fechaFinLDT, inicioList, finList);
            reporte.cerrarDocumento();
            System.out.println("Reporte finalizado.");
        } catch (IllegalArgumentException e) {
            // Manejo de excepción si las fechas no fueron seleccionadas
            System.out.println(e.getMessage());
        }
    }

}
