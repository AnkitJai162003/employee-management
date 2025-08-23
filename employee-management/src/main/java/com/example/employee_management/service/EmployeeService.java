package com.example.employee_management.service;

import org.springframework.stereotype.Service;
import com.example.employee_management.entity.Employee;
import java.util.*;

import com.example.employee_management.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){
        this.employeeRepository=employeeRepository;
    }

    public List<Employee> getAllEmployees(){
            return employeeRepository.findAll();
    }

    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(Long id){
        return employeeRepository.findById(id).orElse(null);
    }

    public void deleteEmployee(Long id){
        employeeRepository.deleteById(id);
    }
    
}
