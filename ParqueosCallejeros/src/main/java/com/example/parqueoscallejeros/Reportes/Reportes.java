package com.example.parqueoscallejeros.Reportes;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
    public void agregarTablaMultas(LocalDateTime fechaInicio, LocalDateTime fechaFin) {

        try {
            PdfPTable tabla = new PdfPTable(5); // 5 columnas: id, id_inspector, placa, costo, fecha
            tabla.addCell("Id Multa");
            tabla.addCell("Id Inspector");
            tabla.addCell("Placa");
            tabla.addCell("Costo");
            tabla.addCell("Fecha Multado");

            MultasDAO multaDAO = new MultasDAO();
            List<Multa> multas = multaDAO.mostrarMultas(fechaInicio, fechaFin); // Asegúrate de que este método existe

            // Verifica que la lista de multas no sea nula
            if (multas != null && !multas.isEmpty()) {
                for (Multa multa : multas) {
                    tabla.addCell(String.valueOf(multa.getId()));               // Agrega el id de la multa
                    tabla.addCell(String.valueOf(multa.getIdInspector()));      // Agrega el id del inspector
                    tabla.addCell(multa.getPlaca());                            // Agrega la placa del vehículo
                    tabla.addCell(String.valueOf(multa.getCosto()));            // Agrega el costo de la multa
                    tabla.addCell(multa.getFechaMultado().toString());          // Agrega la fecha en que se multó

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

    public void agregarTablaHistorialUso(LocalDateTime fechaInicio, LocalDateTime fechaFin) {

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
                    tabla.addCell(String.valueOf(historialDeUso.getId()));               // Agrega el id de la multa
                    tabla.addCell(String.valueOf(historialDeUso.getIdUsuario()));
                    tabla.addCell(String.valueOf(historialDeUso.getIdEspacio()));
                    tabla.addCell(String.valueOf(historialDeUso.getCosto()));            // Agrega el costo de la multa
                    tabla.addCell(String.valueOf(historialDeUso.getTiempoOcupado()));
                    tabla.addCell(historialDeUso.getFechaUso().toString());          // Agrega la fecha en que se multó
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



    public void cerrarDocumento(){
        documento.close();
    }

}




