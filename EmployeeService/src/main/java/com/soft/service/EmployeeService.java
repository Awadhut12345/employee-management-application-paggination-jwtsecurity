package com.soft.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.soft.entity.Employee;
import com.soft.repository.EmployeeRepository;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	public Employee createEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	public List<Employee> fetchAllEmployees() {
		return employeeRepository.findAll();
	}

	public Optional<Employee> fetchEmployeeById(Long id) {
		return employeeRepository.findById(id);
	}

	public Employee updateEmployeeById(Long id, Employee Employee) {
		Employee EmployeeUpdateId = employeeRepository.findById(id).orElseThrow();
		EmployeeUpdateId.setName(Employee.getName());
		return EmployeeUpdateId;
	}

	public Employee updateEmployeePartially(Long id, Employee EmployeeDetails) {
		Employee EmployeeId = employeeRepository.findById(id).orElseThrow();
		EmployeeId.setName(EmployeeDetails.getName());
		return EmployeeId;
	}

	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
	}

	public Page<Employee> getEmployeesSorted(int page, int size, String sortBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return employeeRepository.findAll(pageable);
	}
}
