package com.crud.Reportes;


import com.crud.Login.LoginData;
import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;

import static com.crud.Reportes.GetDataReportes.getDataReportes;

public class GenerarExcel {
    public static String[] titulos = {"Nombre", "Fecha", "Conexiones"};
    public static ObservableList<LoginData> estadisticas;
    public static void generarExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            estadisticas = getDataReportes();
            Sheet sheet = workbook.createSheet("Sheet1");
            Row headerRow = sheet.createRow(0);
            Cell headerCell = headerRow.createCell(0);
            headerCell.setCellValue(titulos[0]);
            headerCell = headerRow.createCell(1);
            headerCell.setCellValue(titulos[1]);
            headerCell = headerRow.createCell(2);
            headerCell.setCellValue(titulos[2]);
            int rowIndex = 1;
            for (LoginData loginData : estadisticas) {
                Row dataRow = sheet.createRow(rowIndex);
                Cell dataCell = dataRow.createCell(0);
                dataCell.setCellValue(loginData.getNombre());
                dataCell = dataRow.createCell(1);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String formattedDate = dateFormat.format(loginData.getFecha());
                dataCell.setCellValue(formattedDate);
                dataCell = dataRow.createCell(2);
                dataCell.setCellValue(loginData.getCantidad());
                rowIndex++;
            }
            FileOutputStream fileOut = new FileOutputStream("tablaReportes.xlsx");
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
