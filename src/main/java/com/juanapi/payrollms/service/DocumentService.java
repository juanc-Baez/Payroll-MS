package com.juanapi.payrollms.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.juanapi.payrollms.model.Nomina;
import com.juanapi.payrollms.model.Recibo;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;

@Service
public class DocumentService {

    private final DecimalFormat currencyFormat = new DecimalFormat("#,##0.00");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public byte[] generarNominaPDF(Nomina nomina) throws IOException, DocumentException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

            // Título del documento
            Paragraph title = new Paragraph("NÓMINA DE EMPLEADOS", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);

            // Información de la nómina
            Paragraph info = new Paragraph("Fecha de generación: " + nomina.getFechaGeneracion().format(dateFormatter), normalFont);
            info.setSpacingAfter(20);
            document.add(info);

            // Crear tabla para los recibos
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            float[] columnWidths = {25f, 20f, 15f, 15f, 25f};
            table.setWidths(columnWidths);

            // Headers de la tabla
            String[] headers = {"Empleado", "Tipo Contrato", "Salario Base", "Deducciones", "Salario Total"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            }

            // Agregar datos de cada recibo
            double totalSalarios = 0;
            double totalDeducciones = 0;
            double totalImpuestos = 0;

            for (Recibo recibo : nomina.getRecibosPago()) {
                table.addCell(new PdfPCell(new Phrase(
                    recibo.getEmpleado().getNombre() + " " + recibo.getEmpleado().getApellido(), normalFont)));
                table.addCell(new PdfPCell(new Phrase(recibo.getEmpleado().getTipoContrato().toString(), normalFont)));
                
                PdfPCell salarioCell = new PdfPCell(new Phrase("$" + currencyFormat.format(recibo.getEmpleado().getSalarioBase()), normalFont));
                salarioCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(salarioCell);
                
                PdfPCell deduccionesCell = new PdfPCell(new Phrase("$" + currencyFormat.format(recibo.getDeducciones()), normalFont));
                deduccionesCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(deduccionesCell);
                
                PdfPCell totalCell = new PdfPCell(new Phrase("$" + currencyFormat.format(recibo.getSalarioTotal()), normalFont));
                totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(totalCell);

                totalSalarios += recibo.getEmpleado().getSalarioBase();
                totalDeducciones += recibo.getDeducciones();
                totalImpuestos += recibo.getImpuestos();
            }

            // Fila de totales
            PdfPCell totalLabelCell = new PdfPCell(new Phrase("TOTALES", headerFont));
            totalLabelCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(totalLabelCell);
            
            PdfPCell emptyCell = new PdfPCell(new Phrase("", normalFont));
            emptyCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(emptyCell);
            
            PdfPCell totalSalariosCell = new PdfPCell(new Phrase("$" + currencyFormat.format(totalSalarios), headerFont));
            totalSalariosCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalSalariosCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(totalSalariosCell);
            
            PdfPCell totalDeduccionesCell = new PdfPCell(new Phrase("$" + currencyFormat.format(totalDeducciones), headerFont));
            totalDeduccionesCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalDeduccionesCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(totalDeduccionesCell);
            
            PdfPCell granTotalCell = new PdfPCell(new Phrase("$" + currencyFormat.format(totalSalarios - totalDeducciones - totalImpuestos), headerFont));
            granTotalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            granTotalCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(granTotalCell);

            document.add(table);

            // Pie de página
            Paragraph footer = new Paragraph("\nDocumento generado automáticamente por el Sistema de Nóminas", normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);

        } finally {
            document.close();
        }
        
        return outputStream.toByteArray();
    }

    public byte[] generarReciboPDF(Recibo recibo) throws IOException, DocumentException {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        
        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, BaseColor.BLACK);
            Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);

            // Título del documento
            Paragraph title = new Paragraph("RECIBO DE PAGO", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(30);
            document.add(title);

            // Información del empleado
            Paragraph empleadoInfo = new Paragraph();
            empleadoInfo.add(new Chunk("Empleado: ", headerFont));
            empleadoInfo.add(new Chunk(recibo.getEmpleado().getNombre() + " " + recibo.getEmpleado().getApellido() + "\n", normalFont));
            empleadoInfo.add(new Chunk("Tipo de Contrato: ", headerFont));
            empleadoInfo.add(new Chunk(recibo.getEmpleado().getTipoContrato().toString() + "\n", normalFont));
            empleadoInfo.add(new Chunk("Fecha: ", headerFont));
            empleadoInfo.add(new Chunk(recibo.getFecha().format(dateFormatter), normalFont));
            empleadoInfo.setSpacingAfter(20);
            document.add(empleadoInfo);

            // Crear tabla para los detalles del pago
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(60);
            float[] columnWidths = {70f, 30f};
            table.setWidths(columnWidths);

            // Detalles del pago
            table.addCell(new PdfPCell(new Phrase("Salario Base:", headerFont)));
            PdfPCell salarioCell = new PdfPCell(new Phrase("$" + currencyFormat.format(recibo.getEmpleado().getSalarioBase()), normalFont));
            salarioCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(salarioCell);

            table.addCell(new PdfPCell(new Phrase("Deducciones:", headerFont)));
            PdfPCell deduccionesCell = new PdfPCell(new Phrase("-$" + currencyFormat.format(recibo.getDeducciones()), normalFont));
            deduccionesCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(deduccionesCell);

            table.addCell(new PdfPCell(new Phrase("Impuestos:", headerFont)));
            PdfPCell impuestosCell = new PdfPCell(new Phrase("-$" + currencyFormat.format(recibo.getImpuestos()), normalFont));
            impuestosCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(impuestosCell);

            // Línea separadora
            PdfPCell separatorCell1 = new PdfPCell(new Phrase("", normalFont));
            separatorCell1.setBorder(Rectangle.NO_BORDER);
            table.addCell(separatorCell1);
            
            PdfPCell separatorCell2 = new PdfPCell(new Phrase("────────────", normalFont));
            separatorCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
            separatorCell2.setBorder(Rectangle.NO_BORDER);
            table.addCell(separatorCell2);

            PdfPCell totalLabelCell = new PdfPCell(new Phrase("TOTAL A PAGAR:", headerFont));
            totalLabelCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(totalLabelCell);
            
            PdfPCell totalCell = new PdfPCell(new Phrase("$" + currencyFormat.format(recibo.getSalarioTotal()), headerFont));
            totalCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            totalCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(totalCell);

            document.add(table);

            // Pie de página
            Paragraph footer = new Paragraph("\n\nDocumento generado automáticamente por el Sistema de Nóminas", normalFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(50);
            document.add(footer);

        } finally {
            document.close();
        }
        
        return outputStream.toByteArray();
    }
}
