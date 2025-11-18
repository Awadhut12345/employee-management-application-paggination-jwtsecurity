package com.soft.scheduler;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.soft.entity.Employee;
import com.soft.repository.EmployeeRepository;

@Service
public class EmployeeSyncService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void processEmployees() {
        System.out.println("Running EmployeeSyncService...");

        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            System.out.println("No employees found in the database.");
            return;
        }

        // Example: Update some field or just log
        employees.forEach(emp -> {
            System.out.println("Processing Employee ID: " + emp.getId() + ", Name: " + emp.getName());
          
        });

        System.out.println("Processed " + employees.size() + " employees.");
    }
}
