package com.example.parqueoscallejeros.Reportes;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;


public class Reportes {
    Document documento;
    FileOutputStream fileOutputStream;

    //Fuente del titulo y texto
    Font fuenteTitulo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 16);
    Font fuenteParrafo = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12);

    public static String time() {
        // Obtiene la fecha y hora actual
        LocalDateTime now = LocalDateTime.now();
        // Formato deseado para la fecha y hora
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy_MM_dd HH_mm_ss");
        // Retorna la fecha y hora como una cadena formateada
        return now.format(formatter);
    }


    public void crearDocumento(String reporte) throws FileNotFoundException, DocumentException {
        documento = new Document(PageSize.A4, 35,30,50,50);

        //archivo pdf que se va generar
        String ruta = System.getProperty("user.home");
        fileOutputStream = new FileOutputStream(ruta + "/Desktop/"+reporte+ time() +".pdf");
        PdfWriter.getInstance(documento, fileOutputStream); // Asocia el PDF writer al documento
    }
    public void abrirDocumento() throws FileNotFoundException {
        documento.open();
    }

    public void agregarTitulo(String parrafo) throws FileNotFoundException, DocumentException {
        PdfPTable tabla = new PdfPTable(1);
        PdfPCell celda = new PdfPCell(new Phrase(parrafo, fuenteTitulo));
        celda.setColspan(5);
        celda.setBorderColor(BaseColor.WHITE);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
        documento.add(tabla);
    }
    public void agregarTexto(String texto) throws FileNotFoundException, DocumentException {
        Paragraph parrafo = new Paragraph();
        parrafo.add(new Phrase(texto, fuenteParrafo));
        documento.add(parrafo);
    }
    public void agregarSaltosDeLinea() throws DocumentException {
        Paragraph saltodeLinea = new Paragraph();
        saltodeLinea.add(new Phrase(Chunk.NEWLINE));
        saltodeLinea.add(new Phrase(Chunk.NEWLINE));
        documento.add(saltodeLinea);
    }
    public void agregarTablaMultas(LocalDateTime fechaInicio, LocalDateTime fechaFin, int userId) {
        try {
            PdfPTable tabla = new PdfPTable(5); // 5 columnas: id, id_inspector, placa, costo, fecha
            tabla.addCell("Id Multa");
            tabla.addCell("Id Inspector");
            tabla.addCell("Placa");
            tabla.addCell("Costo");
            tabla.addCell("Fecha Multado");

            MultasDAO multaDAO = new MultasDAO();
            List<Multa> multas = multaDAO.mostrarMultas(fechaInicio, fechaFin);

            VehiculoDAO vehiculoDAO = new VehiculoDAO();
            List<Vehiculo> vehiculos = vehiculoDAO.mostrarVehiculos(); // Asegúrate de que el método existe

            // Verifica que la lista de multas no sea nula
            if (multas != null && !multas.isEmpty()) {
                for (Multa multa : multas) {
                    boolean multaPerteneceAlUsuario = false;

                    for (Vehiculo vehiculo : vehiculos) {
                        if (multa.getPlaca().equals(vehiculo.getPlaca()) && (vehiculo.getIdUsuario() == userId || userId == 0)) {
                            multaPerteneceAlUsuario = true; // Al menos una multa pertenece al usuario
                            break; // Salimos del bucle, ya no es necesario seguir buscando
                        } else if (userId== multa.getIdInspector()) {
                            multaPerteneceAlUsuario = true;
                            break;
                        }
                    }

                    if (multaPerteneceAlUsuario) {
                        tabla.addCell(String.valueOf(multa.getId()));               // Agrega el id de la multa
                        tabla.addCell(String.valueOf(multa.getIdInspector()));      // Agrega el id del inspector
                        tabla.addCell(multa.getPlaca());                            // Agrega la placa del vehículo
                        tabla.addCell(String.valueOf(multa.getCosto()));            // Agrega el costo de la multa
                        tabla.addCell(multa.getFechaMultado().toString());          // Agrega la fecha en que se multó
                    }
                }
            } else {
                // Manejar el caso en que no hay multas
                tabla.addCell("No se encontraron multas.");
                for (int i = 0; i < 4; i++) {
                    tabla.addCell(""); // Agrega celdas vacías para completar las columnas
                }
            }

            documento.add(tabla);
        } catch (DocumentException e) {
            // Manejo más robusto de excepciones
            System.err.println("Error al agregar la tabla de multas: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void agregarTablaDineroMultas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {

        try {
            PdfPTable tabla = new PdfPTable(3); // 3 columnas: id, costo, fecha
            tabla.addCell("Id Multa");
            tabla.addCell("Costo");
            tabla.addCell("Fecha Multado");

            MultasDAO multaDAO = new MultasDAO();
            List<Multa> multas = multaDAO.mostrarMultas(fechaInicio, fechaFin); // Asegúrate de que este método existe

            double totalGanancias = 0.0; // Variable para almacenar el total de ganancias

            // Verifica que la lista de multas no sea nula
            if (multas != null && !multas.isEmpty()) {
                for (Multa multa : multas) {
                    tabla.addCell(String.valueOf(multa.getId()));               // Agrega el id de la multa
                    tabla.addCell(String.valueOf(multa.getCosto()));            // Agrega el costo de la multa
                    tabla.addCell(multa.getFechaMultado().toString());          // Agrega la fecha en que se multó

                    // Sumar el costo de la multa al total
                    totalGanancias += multa.getCosto();
                }
            } else {
                // Manejar el caso en que no hay multas
                tabla.addCell("No se encontraron multas.");
                for (int i = 0; i < 4; i++) {
                    tabla.addCell(""); // Agrega celdas vacías para completar las columnas
                }
            }

            // Agregar fila para mostrar el total de ganancias
            tabla.addCell(""); // Espacio vacío en la primera columna
            tabla.addCell("Total Ganancias:"); // Texto fijo en la cuarta columna
            tabla.addCell(String.valueOf(totalGanancias)); // Total en la quinta columna

            documento.add(tabla);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void agregarTablaHistorialUso(LocalDateTime fechaInicio, LocalDateTime fechaFin, int idUser) {

        try {
            PdfPTable tabla = new PdfPTable(6); // 3 columnas: id, costo, fecha
            tabla.addCell("Id");
            tabla.addCell("Id Usuario");
            tabla.addCell("Id Espacio");
            tabla.addCell("Costo");
            tabla.addCell("Tiempo Ocupado");
            tabla.addCell("Fecha de Uso");

            HistorialDeUsoDAO historialDeUsoDAO = new HistorialDeUsoDAO();
            List<HistorialDeUso> listhistorialDeUso = historialDeUsoDAO.mostrarHistorialUso(fechaInicio, fechaFin); // Asegúrate de que este método existe

            // Verifica que la lista de multas no sea nula
            if (listhistorialDeUso != null && !listhistorialDeUso.isEmpty()) {
                for (HistorialDeUso historialDeUso : listhistorialDeUso) {
                    if (historialDeUso.getId() == idUser || idUser==0) {
                        tabla.addCell(String.valueOf(historialDeUso.getId()));               // Agrega el id de la multa
                        tabla.addCell(String.valueOf(historialDeUso.getIdUsuario()));
                        tabla.addCell(String.valueOf(historialDeUso.getIdEspacio()));
                        tabla.addCell(String.valueOf(historialDeUso.getCosto()));            // Agrega el costo de la multa
                        tabla.addCell(String.valueOf(historialDeUso.getTiempoOcupado()));
                        tabla.addCell(historialDeUso.getFechaUso().toString());          // Agrega la fecha en que se multó
                    }

                }
            } else {
                // Manejar el caso en que no hay multas
                tabla.addCell("No se encontraron registros.");
                for (int i = 0; i < 4; i++) {
                    tabla.addCell(""); // Agrega celdas vacías para completar las columnas
                }
            }
            documento.add(tabla);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void agregarTablaIngresoDinero(LocalDateTime fechaInicio, LocalDateTime fechaFin,boolean total) {

        try {
            PdfPTable tabla = new PdfPTable(4); // 3 columnas: id, costo, fecha
            tabla.addCell("Id Espacio");
            tabla.addCell("Costo");
            tabla.addCell("Tiempo Reservado");
            tabla.addCell("Fecha de Reserva");

            ReservaDao reservaDao = new ReservaDao();
            List<Reserva> reservas = reservaDao.mostrarReserva(fechaInicio, fechaFin); // Asegúrate de que este método existe

            double totalGanancias = 0.0; // Variable para almacenar el total de ganancias

            // Verifica que la lista de multas no sea nula
            if (reservas != null && !reservas.isEmpty()) {
                for (Reserva reserva : reservas) {
                    tabla.addCell(String.valueOf(reserva.getIdEspacio()));               // Agrega el id de la reserva
                    tabla.addCell(String.valueOf(reserva.getCosto()));            // Agrega el costo de la reserva
                    tabla.addCell(String.valueOf(reserva.getTiempoReserva()));
                    tabla.addCell(reserva.getFecha().toString());          // Agrega la fecha en que se reservo

                    // Sumar el costo de la multa al total
                    totalGanancias += reserva.getCosto();
                }
            } else {
                // Manejar el caso en que no hay multas
                tabla.addCell("No se encontraron Reservas.");
                for (int i = 0; i < 4; i++) {
                    tabla.addCell(""); // Agrega celdas vacías para completar las columnas
                }
            }

            // Agregar fila para mostrar el total de ganancias
            if (total) {
                tabla.addCell("Total Ganancias:"); // Texto fijo en la cuarta columna
                tabla.addCell(String.valueOf(totalGanancias)); // Total en la quinta columna
                tabla.addCell("");
                tabla.addCell("");
            }

            documento.add(tabla);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void agregarTablaEspaciosParqueo(int estado) {
        try {
            PdfPTable tabla = new PdfPTable(2); // 3 columnas: id, costo, fecha
            tabla.addCell("Id");
            tabla.addCell("Numero de Espacio");

            EspaciosParqueoDAO espaciosParqueoDAO = new EspaciosParqueoDAO();
            List<EspaciosParqueo> espaciosParqueos = espaciosParqueoDAO.mostrarEspaciosParqueo(estado); // Asegúrate de que este método existe

            double totalGanancias = 0.0; // Variable para almacenar el total de ganancias

            // Verifica que la lista de multas no sea nula
            if (espaciosParqueos != null && !espaciosParqueos.isEmpty()) {
                for (EspaciosParqueo espaciosParqueo : espaciosParqueos) {
                    tabla.addCell(String.valueOf(espaciosParqueo.getIdParqueo()));               // Agrega el id del parqueo
                    tabla.addCell(String.valueOf(espaciosParqueo.getNumeroEspacio()));            // Agrega el nuemro de parqueo
                }
            } else {
                // Manejar el caso en que no hay multas
                tabla.addCell("No se encontraron parqueos.");
                for (int i = 0; i < 4; i++) {
                    tabla.addCell(""); // Agrega celdas vacías para completar las columnas
                }
            }
            documento.add(tabla);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void agregarTablaEstDetallada(LocalDateTime fechaInicio, LocalDateTime fechaFin, int inicioList, int finList) {
        try {
            PdfPTable tabla = new PdfPTable(4); // 4 columnas: Numero de Espacio, Fecha, Horas Ocupadas, Horas Vacias
            tabla.addCell("Numero de Espacio");
            tabla.addCell("Fecha");
            tabla.addCell("Horas Ocupadas");
            tabla.addCell("Horas Vacias");
            // Obtener los datos
            EspaciosParqueoDAO espaciosParqueoDAO = new EspaciosParqueoDAO();
            List<EspaciosParqueo> espaciosParqueos = espaciosParqueoDAO.mostrarEspaciosParqueo(2);
            HistorialDeUsoDAO historialDeUsoDAO = new HistorialDeUsoDAO();
            List<HistorialDeUso> historialDeUsos = historialDeUsoDAO.mostrarHistorialUso(fechaInicio, fechaFin);
            // Verifica que la lista de espacios no sea nula
            if (espaciosParqueos != null && !espaciosParqueos.isEmpty()) {
                for (EspaciosParqueo espaciosParqueo : espaciosParqueos) {
                    double horasAcumuladas = 0;
                    LocalDate ultimaFecha = null; // Reiniciar `ultimaFecha` para cada espacio
                    for (HistorialDeUso historialDeUso : historialDeUsos) {
                        // Comprobar si el espacio del historial coincide con el espacio actual
                        if (espaciosParqueo.getNumeroEspacio() == historialDeUso.getIdEspacio()) {
                            // Convertir Date a LocalDateTime para una comparación consistente
                            LocalDateTime fechaUso = historialDeUso.getFechaUso().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDateTime();
                            // Comparar fechas dentro del rango usando LocalDateTime
                            if (!fechaUso.isBefore(fechaInicio) && !fechaUso.isAfter(fechaFin)) {
                                // Si es la misma fecha que la última, acumular horas
                                if (ultimaFecha != null && fechaUso.toLocalDate().equals(ultimaFecha)) {
                                    horasAcumuladas += historialDeUso.getTiempoOcupado();
                                } else {
                                    // Si es una nueva fecha, insertar la información acumulada
                                    if (ultimaFecha != null) {
                                        // Añadir la fila acumulada para la última fecha
                                        tabla.addCell(String.valueOf(espaciosParqueo.getNumeroEspacio()));
                                        tabla.addCell(ultimaFecha.toString()); // Muestra la última fecha acumulada
                                        tabla.addCell(String.valueOf(horasAcumuladas / 60)); // Horas ocupadas
                                        tabla.addCell(String.valueOf(24 - (horasAcumuladas / 60))); // Horas vacías
                                    }
                                    // Reiniciar la acumulación para la nueva fecha
                                    ultimaFecha = fechaUso.toLocalDate();
                                    horasAcumuladas = historialDeUso.getTiempoOcupado();
                                }
                            }
                        }
                    }
                    // Añadir la última fila para el espacio, si existe
                    if (ultimaFecha != null) {
                        tabla.addCell(String.valueOf(espaciosParqueo.getNumeroEspacio()));
                        tabla.addCell(ultimaFecha.toString());
                        tabla.addCell(String.valueOf(horasAcumuladas / 60)); // Horas ocupadas
                        tabla.addCell(String.valueOf(24 - (horasAcumuladas / 60))); // Horas vacías
                    }
                }
            } else {
                // Si no hay espacios, agregar un mensaje
                tabla.addCell("No se encontraron parqueos.");
                for (int i = 0; i < 3; i++) {
                    tabla.addCell(""); // Rellenar las otras columnas vacías
                }
            }

            // Agregar la tabla al documento
            documento.add(tabla);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void agregarTablaEstResumida(LocalDateTime fechaInicio, LocalDateTime fechaFin, int inicioList, int finList) {
        try {
            PdfPTable tabla = new PdfPTable(4); // 4 columnas: Fecha, Numero de Espacio, Horas Ocupadas, Horas Vacias
            tabla.addCell("Fecha");
            tabla.addCell("Numero de Espacio");
            tabla.addCell("Horas Ocupadas");
            tabla.addCell("Horas Vacias");
            // Obtener los datos
            EspaciosParqueoDAO espaciosParqueoDAO = new EspaciosParqueoDAO();
            List<EspaciosParqueo> espaciosParqueos = espaciosParqueoDAO.mostrarEspaciosParqueo(2);
            HistorialDeUsoDAO historialDeUsoDAO = new HistorialDeUsoDAO();
            List<HistorialDeUso> historialDeUsos = historialDeUsoDAO.mostrarHistorialUso(fechaInicio, fechaFin);
            // Iterar sobre cada día entre fechaInicio y fechaFin
            LocalDateTime currentDay = fechaInicio;
            while (!currentDay.isAfter(fechaFin)) {
                // Convertir currentDay a Date para facilitar las comparaciones
                LocalDate currentDate = currentDay.toLocalDate();
                // Recorrer los espacios de parqueo
                for (EspaciosParqueo espaciosParqueo : espaciosParqueos) {
                    double horasAcumuladas = 0;
                    // Recorrer el historial de uso para verificar el uso en la fecha actual
                    for (HistorialDeUso historialDeUso : historialDeUsos) {
                        // Comprobar si el espacio del historial coincide con el espacio actual
                        if (espaciosParqueo.getNumeroEspacio() == historialDeUso.getIdEspacio()) {
                            // Convertir `Date` a `LocalDate` para la comparación
                            LocalDate fechaUso = historialDeUso.getFechaUso().toInstant()
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
                            // Si la fecha coincide, acumular las horas ocupadas
                            if (fechaUso.equals(currentDate)) {
                                horasAcumuladas += historialDeUso.getTiempoOcupado();
                            }
                        }
                    }
                    // Añadir la fila con los datos del día actual y espacio
                    tabla.addCell(currentDate.toString()); // Fecha
                    tabla.addCell(String.valueOf(espaciosParqueo.getNumeroEspacio())); // Numero de Espacio
                    tabla.addCell(String.valueOf(horasAcumuladas / 60)); // Horas ocupadas
                    tabla.addCell(String.valueOf(24 - (horasAcumuladas / 60))); // Horas vacías
                }
                // Avanzar al siguiente día
                currentDay = currentDay.plusDays(1);
            }
            // Agregar la tabla al documento
            documento.add(tabla);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }




    public void cerrarDocumento(){
        documento.close();
    }

}




