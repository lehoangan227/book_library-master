package com.project.Book.util.excel;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Date;
import java.util.List;
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BaseExport<T> {
    Workbook workbook;
    Sheet sheet;
    List<T> listData;

    public BaseExport(List<T> listData) {
        this.listData = listData;
        this.workbook = new HSSFWorkbook();
    }

    public BaseExport<T> writeHeaderLine(String[] headers){
        sheet = workbook.createSheet();
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 12);
        font.setFontName("Times New Roman");
        font.setColor(IndexedColors.BLUE.getIndex());
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        for(int i = 0; i < headers.length; i++){
            createCell(row, i, headers[i], style);
        }
        return this;
    }

    public void createCell(Row row, int columnCount, Object value, CellStyle style){
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if(value instanceof Integer){
            cell.setCellValue((Integer)value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean)value);
        }else if (value instanceof Date){
            cell.setCellValue(value.toString());
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    public BaseExport<T> writeDateLine(String[] fields, Class<T> clazz){
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 12);
        font.setFontName("Times New Roman");
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        for(var data : this.listData){
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for(String fieldName : fields){
                try {
                    Field field =clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object value = field.get(data);
                    createCell(row, columnCount, value, style);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                columnCount++;
            }
        }
        return this;
    }

    public void export(HttpServletResponse response) throws IOException {
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        workbook.close();
        out.close();
    }

    public void exportBase64(HttpServletResponse response) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        String base64EncodedString = Base64.getEncoder().encodeToString(byteArray);
        ServletOutputStream out = response.getOutputStream();
        out.write(base64EncodedString.getBytes());
        out.close();
    }
}
