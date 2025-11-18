package com.soft.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.soft.entity.Employee;
import com.soft.repository.EmployeeRepository;


@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	  @Autowired
	    private EmailService emailService;

	    @Autowired
	    private WhatsAppService whatsappService;

	public Employee createEmployee(Employee employee) {
		 Employee savedEmployee = employeeRepository.save(employee);

	        // Email Notification
	        emailService.sendEmployeeRegistrationEmail(
	                "awadhutyenkar@gmail.com",    
	                savedEmployee.getName()
	        );

	        // WhatsApp notification
	        whatsappService.sendMessage(
	                "+91 8459682058",   // Country code + number
	                "Welcome " + savedEmployee.getName() + "! Your employee account is created."
	        );

	        return savedEmployee;
	}

	public List<Employee> fetchAllEmployees() {
		return employeeRepository.findAll();
	}

	public Optional<Employee> fetchEmployeeById(Long id) {
		return employeeRepository.findById(id);
	}

	public Employee updateEmployeeById(Long id, Employee employeeDetails) {
	    Employee employee = employeeRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Employee not found with id " + id));

	    employee.setName(employeeDetails.getName());
	    // Save updated record
	    return employeeRepository.save(employee);
	}


	public Employee updateEmployeePartially(Long id, Employee employeeDetails) {
	    Employee employee = employeeRepository.findById(id)
	            .orElseThrow(() -> new RuntimeException("Employee not found with id " + id));

	    if (employeeDetails.getName() != null)
	        employee.setName(employeeDetails.getName());

	    return employeeRepository.save(employee);
	}


	public void deleteEmployee(Long id) {
		employeeRepository.deleteById(id);
	}

	public Page<Employee> getEmployeesSorted(int page, int size, String sortBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return employeeRepository.findAll(pageable);
	}
}
