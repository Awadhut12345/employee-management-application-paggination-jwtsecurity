package com.soft.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import com.soft.entity.Employee;
import com.soft.repository.EmployeeRepository;

class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
    }

    @Test
    void testCreateEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        Employee saved = employeeService.createEmployee(employee);
        assertEquals("John Doe", saved.getName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testFetchAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(employee));
        List<Employee> result = employeeService.fetchAllEmployees();
        assertEquals(1, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testFetchEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        Optional<Employee> found = employeeService.fetchEmployeeById(1L);
        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getName());
    }

    @Test
    void testUpdateEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee update = new Employee();
        update.setName("Updated Name");

        Employee result = employeeService.updateEmployeeById(1L, update);

        assertEquals("Updated Name", result.getName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1L);
        employeeService.deleteEmployee(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetEmployeesSorted() {
        Page<Employee> page = new PageImpl<>(Arrays.asList(employee));
        when(employeeRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Employee> result = employeeService.getEmployeesSorted(0, 5, "id");
        assertEquals(1, result.getContent().size());
        verify(employeeRepository, times(1)).findAll(any(Pageable.class));
    }
}
