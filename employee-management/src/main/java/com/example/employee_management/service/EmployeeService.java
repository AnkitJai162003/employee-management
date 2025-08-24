package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.EmployeeRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.employee_management.specification.EmployeeSpecification.hasDepartment;
import static com.example.employee_management.specification.EmployeeSpecification.hasName;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Convert Entity to DTO
    private EmployeeDTO convertToDTO(Employee employee) {
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getEmail(), employee.getDepartment());
    }

    // Create or Update Employee
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee(employeeDTO.getId(), employeeDTO.getName(), employeeDTO.getEmail(), employeeDTO.getDepartment());
        return convertToDTO(employeeRepository.save(employee));
    }

    // Get All Employees
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Get Employee By ID
    public EmployeeDTO getEmployeeById(Long id) {
        return employeeRepository.findById(id).map(this::convertToDTO).orElse(null);
    }

    // Delete Employee
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // Pagination + Sorting
    public Page<EmployeeDTO> getEmployeesPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeRepository.findAll(pageable).map(this::convertToDTO);
    }

    // Filtering + Pagination + Sorting
    public Page<EmployeeDTO> getEmployeesWithFilter(String name, String department, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Employee> spec = Specification.where(hasName(name)).and(hasDepartment(department));

        return employeeRepository.findAll(spec, pageable).map(this::convertToDTO);
    }
}


