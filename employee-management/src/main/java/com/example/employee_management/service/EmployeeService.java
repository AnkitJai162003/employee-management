package com.example.employee_management.service;

import com.example.employee_management.dto.EmployeeDTO;
import com.example.employee_management.entity.Employee;
import com.example.employee_management.exception.EmployeeNotFoundException;
import com.example.employee_management.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//@Slf4j
@Service
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);


    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;

    public EmployeeService(EmployeeRepository employeeRepository,NotificationService notificationService) {
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getEmail(), employee.getDepartment());
    }

//    @CachePut(value = "employees", key = "#employeeDTO.id")
    public EmployeeDTO saveEmployee(EmployeeDTO employeeDTO) {
        logger.info("Saving employee with name: {}", employeeDTO.getName());
        Employee employee = new Employee(
                employeeDTO.getId(),
                employeeDTO.getName(),
                employeeDTO.getEmail(),
                employeeDTO.getDepartment()
        );

        EmployeeDTO savedEmployee = convertToDTO(employeeRepository.save(employee));

        // Trigger async notification (does not block response)
        notificationService.sendEmployeeNotification(savedEmployee.getName());

        return savedEmployee;
    }

//    @Cacheable(value = "employeesList")
    public List<EmployeeDTO> getAllEmployees() {
        logger.debug("Fetching all employees from database or cache");
        return employeeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

//    @Cacheable(value = "employees", key = "#id")
    public EmployeeDTO getEmployeeById(Long id) {
        logger.info("Fetching employee with ID: {}", id);
        return employeeRepository.findById(id).map(this::convertToDTO)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found"));
    }

//    @CacheEvict(value = {"employees", "employeesList"}, allEntries = true)
    public void deleteEmployee(Long id) {
        logger.warn("Deleting employee with ID: {}", id);

        employeeRepository.deleteById(id);

        notificationService.sendEmployeeNotification("Deleted Employee ID: " + id);
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