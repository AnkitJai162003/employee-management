package com.example.employee_management.controller;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.service.EmployeeService;
// at class top:
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;



import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Get all employees
    // then above any method you want restricted:
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<EmployeeDTO> getAllEmployees() {
        log.info("Request received: Fetching all employees");
        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        log.debug("Fetched {} employees", employees.size());
        return employees;
    }

    // Create Employee
    @PostMapping
    public EmployeeDTO createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        log.info("Request received: Creating employee with name={}", employeeDTO.getName());
        EmployeeDTO savedEmployee = employeeService.saveEmployee(employeeDTO);
        log.info("Employee created successfully with id={}", savedEmployee.getId());
        return savedEmployee;
    }

    // Get Employee by ID
    @GetMapping("/{id}")
    public EmployeeDTO getEmployeeById(@PathVariable Long id) {
        log.info("Request received: Fetching employee with id={}", id);
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        log.debug("Fetched employee details: {}", employee);
        return employee;

    }

    // Delete Employee
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        log.warn("Request received: Deleting employee with id={}", id);
        employeeService.deleteEmployee(id);
        log.info("Employee with id={} deleted successfully", id);
        return "Employee deleted successfully";
    }

    // Pagination + Sorting
    @GetMapping("/paginated")
    public Page<EmployeeDTO> getEmployeesPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Request received: Paginated fetch - page={}, size={}, sortBy={}, sortDir={}",
                page, size, sortBy, sortDir);
        Page<EmployeeDTO> paginatedResult = employeeService.getEmployeesPaginated(page, size, sortBy, sortDir);
        log.debug("Paginated fetch result size={}", paginatedResult.getTotalElements());
        return paginatedResult;
    }

    // Filtering + Pagination + Sorting
    @GetMapping("/filter")
    public Page<EmployeeDTO> getFilteredEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        log.info("Request received: Filtered fetch - name={}, department={}, page={}, size={}",
                name, department, page, size);
        Page<EmployeeDTO> filteredResult = employeeService.getEmployeesWithFilter(name, department, page, size, sortBy, sortDir);
        log.debug("Filtered fetch result size={}", filteredResult.getTotalElements());
        return filteredResult;
    }
}

