package com.soft.service;

import com.soft.entity.Employee;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public String exportEmployeesToExcel(List<Employee> employees) {
        String filePath = "employees.xlsx"; // saved at project root
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Employees");

        // Header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Name");

        // Data rows
        int rowNum = 1;
        for (Employee emp : employees) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(emp.getId());
            row.createCell(1).setCellValue(emp.getName());
        }

        // Autosize columns
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
            workbook.close();
            return "✅ Excel file created successfully at: " + filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return "❌ Error creating Excel file: " + e.getMessage();
        }
    }
}
