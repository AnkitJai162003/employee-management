package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.repository.EmployeeRepository;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.employee_management.specification.EmployeeSpecification.hasDepartment;
import static com.example.employee_management.specification.EmployeeSpecification.hasName;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getEmail(), employee.getDepartment());
    }

    @CachePut(value = "employees", key = "#employeeDTO.id")
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        Employee employee = new Employee(employeeDTO.getId(), employeeDTO.getName(), employeeDTO.getEmail(), employeeDTO.getDepartment());
        return convertToDTO(employeeRepository.save(employee));
    }

    @Cacheable(value = "employeesList")
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Cacheable(value = "employees", key = "#id")
    public EmployeeDTO getEmployeeById(Long id) {
        return employeeRepository.findById(id).map(this::convertToDTO)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found"));
    }

    @CacheEvict(value = {"employees", "employeesList"}, allEntries = true)
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public Page<EmployeeDTO> getEmployeesPaginated(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return employeeRepository.findAll(pageable).map(this::convertToDTO);
    }

    public Page<EmployeeDTO> getEmployeesWithFilter(String name, String department, int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Specification<Employee> spec = Specification.where(hasName(name)).and(hasDepartment(department));
        return employeeRepository.findAll(spec, pageable).map(this::convertToDTO);
    }
}


