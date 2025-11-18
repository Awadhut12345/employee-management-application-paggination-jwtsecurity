package com.soft.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.soft.entity.Employee;
import com.soft.exception.EmployeeNotFoundException;
import com.soft.service.EmployeeService;
import com.soft.service.ExcelExportService;

import jakarta.validation.Valid;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;
	
	@Autowired
	private ExcelExportService excelExportService;

	@PostMapping("/msg")
	public String getMsg() {
		return "Message";
	}

	@PostMapping("/save")
	public ResponseEntity<?> createEmployee(@Valid @RequestBody Employee employee) {
		try {
			Employee savedEmployee = employeeService.createEmployee(employee);
			return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
		} catch (Exception ex) {
			throw new RuntimeException("Error while saving employee: " + ex.getMessage());
		}
	}

	@GetMapping("/getAllEmployees")
	public ResponseEntity<?> getAllEmployees() {
		try {
			List<Employee> employees = employeeService.fetchAllEmployees();
			if (employees.isEmpty()) {
				throw new EmployeeNotFoundException("No employees found in the database.");
			}
			return ResponseEntity.ok(employees);
		} catch (EmployeeNotFoundException ex) {
			throw ex; // handled by GlobalExceptionHandler
		} catch (Exception ex) {
			throw new RuntimeException("Error fetching employees: " + ex.getMessage());
		}
	}

	@GetMapping("/getEmployee/{id}")
	public ResponseEntity<?> getEmployeeById(@PathVariable Long id) {
		try {
			Employee employee = employeeService.fetchEmployeeById(id)
					.orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
			return ResponseEntity.ok(employee);
		} catch (EmployeeNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException("Error retrieving employee: " + ex.getMessage());
		}
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateEmployee(@PathVariable Long id, @Valid @RequestBody Employee employee) {
		try {
			Employee updated = employeeService.updateEmployeeById(id, employee);
			return ResponseEntity.ok(updated);
		} catch (EmployeeNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException("Error updating employee: " + ex.getMessage());
		}
	}

	@PatchMapping("/updatePar/{id}")
	public ResponseEntity<?> updateEmployeePartially(@PathVariable Long id, @RequestBody Employee employee) {
		try {
			Employee updated = employeeService.updateEmployeePartially(id, employee);
			return ResponseEntity.ok(updated);
		} catch (EmployeeNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException("Error partially updating employee: " + ex.getMessage());
		}
	}

	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
		try {
			employeeService.deleteEmployee(id);
			Map<String, String> response = new HashMap<>();
			response.put("message", "Employee deleted successfully.");
			return ResponseEntity.ok(response);
		} catch (EmployeeNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException("Error deleting employee: " + ex.getMessage());
		}
	}

	@GetMapping("/getEmp")
	public ResponseEntity<?> getEmployees(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "id") String sortBy) {

		try {
			Page<Employee> employeePage = employeeService.getEmployeesSorted(page, size, sortBy);

			if (employeePage.isEmpty()) {
				throw new EmployeeNotFoundException("No employees found for the given page and sort options.");
			}
			Map<String, Object> response = new HashMap<>();
			response.put("employees", employeePage.getContent());
			response.put("currentPage", employeePage.getNumber());
			response.put("totalItems", employeePage.getTotalElements());
			response.put("totalPages", employeePage.getTotalPages());

			return ResponseEntity.ok(response);
		} catch (EmployeeNotFoundException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new RuntimeException("Error fetching paginated employees: " + ex.getMessage());
		}
	}


	@GetMapping("/export/excel")
	public ResponseEntity<String> exportEmployeesToExcel() {
	    List<Employee> employees = employeeService.fetchAllEmployees();
	    if (employees.isEmpty()) {
	        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No employees found to export.");
	    }

	    String message = excelExportService.exportEmployeesToExcel(employees);
	    return ResponseEntity.ok(message);
	}
	
	@GetMapping("/download/excel")
	public ResponseEntity<byte[]> downloadEmployeesExcel() {
	    List<Employee> employees = employeeService.fetchAllEmployees();

	    try (Workbook workbook = new XSSFWorkbook()) {
	        Sheet sheet = workbook.createSheet("Employees");

	        Row header = sheet.createRow(0);
	        header.createCell(0).setCellValue("ID");
	        header.createCell(1).setCellValue("Name");

	        int rowNum = 1;
	        for (Employee emp : employees) {
	            Row row = sheet.createRow(rowNum++);
	            row.createCell(0).setCellValue(emp.getId());
	            row.createCell(1).setCellValue(emp.getName());
	        }

	        sheet.autoSizeColumn(0);
	        sheet.autoSizeColumn(1);

	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        workbook.write(out);

	        return ResponseEntity.ok()
	                .header("Content-Disposition", "attachment; filename=employees.xlsx")
	                .body(out.toByteArray());

	    } catch (IOException e) {
	        return ResponseEntity.internalServerError().build();
	    }
	}


}
