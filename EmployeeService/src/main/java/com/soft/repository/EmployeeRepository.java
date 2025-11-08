package com.soft.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.soft.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
