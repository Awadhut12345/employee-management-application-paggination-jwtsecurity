package com.soft.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import com.soft.service.EmployeeService;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping("/msg")
	public String getMsg() {
		return "Message";
	}

	@PostMapping("/save")
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
		Employee saveEmployee = employeeService.createEmployee(employee);
		return new ResponseEntity<>(saveEmployee, HttpStatus.OK);
	}

	@GetMapping("/getAllEmployees")
	public List<Employee> getAllEmployees() {
		return employeeService.fetchAllEmployees();
	}

	@GetMapping("/getEmployee/{id}")
	public Optional<Employee> getEmployeesById(@PathVariable Long id) {
		Optional<Employee> employeeId = employeeService.fetchEmployeeById(id);
		return employeeId;
	}

	@PutMapping("/update/{id}")
	public Employee updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
		Employee updateEmployeeById = employeeService.updateEmployeeById(id, employee);
		return updateEmployeeById;
	}

	@PatchMapping("/updatePar/{id}")
	public Employee updateEmployeePartially(@PathVariable Long id, @RequestBody Employee employee) {
		Employee updateEmployeePartially = employeeService.updateEmployeePartially(id, employee);
		return updateEmployeePartially;
	}

	@DeleteMapping("/delete/{id}")
	public String deleteEmployee(@PathVariable Long id) {
		employeeService.deleteEmployee(id);
		return "Delete Employee Successfully!";
	}

	@GetMapping("/getEmp")
	public ResponseEntity<Map<String, Object>> getAllEmployees(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "id") String sortBy) {

		Page<Employee> employeePage = employeeService.getEmployeesSorted(page, size, sortBy);

		Map<String, Object> response = new HashMap<>();
		response.put("employees", employeePage.getContent());
		response.put("currentPage", employeePage.getNumber());
		response.put("totalItems", employeePage.getTotalElements());
		response.put("totalPages", employeePage.getTotalPages());

		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
