package com.crud.Reportes;

import com.crud.Login.LoginData;
import javafx.collections.ObservableList;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import static com.crud.Reportes.GetDataReportes.getDataReportes;

public class GenerarPDF {

    public static String[] titulos = {"Nombre", "Fecha", "Conexiones"};
    public static ObservableList<LoginData> estadisticas;
    public static void generarPdf() {
        try {
            estadisticas = getDataReportes();
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            float tituloWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("Reporte de Estadísticas") / 1000f * 18;
            float paginaWidth = page.getMediaBox().getWidth();
            float tituloX = (paginaWidth - tituloWidth) / 2;
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(tituloX, page.getMediaBox().getHeight() - 30);
            contentStream.showText("Reporte de Estadísticas");
            contentStream.endText();
            float[] posicionesEncabezados = new float[titulos.length];
            for (int i = 0; i < titulos.length; i++) {
                contentStream.beginText();
                float anchoColumna = paginaWidth / titulos.length;
                float posicionX = anchoColumna * i + (anchoColumna - PDType1Font.HELVETICA_BOLD.getStringWidth(titulos[i]) / 1000f * 12) / 2;
                contentStream.newLineAtOffset(posicionX, page.getMediaBox().getHeight() - 80);
                contentStream.showText(titulos[i]);
                contentStream.endText();
                posicionesEncabezados[i] = posicionX;
            }

            for (int i = 0; i < estadisticas.size(); i++) {
                    int minHeigth = 300;
                    contentStream.beginText();
                    contentStream.newLineAtOffset(posicionesEncabezados[0], minHeigth + (i * 40));
                    contentStream.showText(estadisticas.get(i).getNombre());
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.newLineAtOffset(posicionesEncabezados[1], minHeigth + (i * 40));
                    contentStream.showText(String.valueOf(estadisticas.get(i).getFecha()));
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.newLineAtOffset(posicionesEncabezados[2], minHeigth + (i * 40));
                    contentStream.showText(String.valueOf(estadisticas.get(i).getCantidad()));
                    contentStream.endText();
            }
            contentStream.close();
            document.save("tablaReportes.pdf");
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

