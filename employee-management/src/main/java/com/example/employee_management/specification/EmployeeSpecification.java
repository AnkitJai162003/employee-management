package com.example.employee_management.specification;

import com.example.employee_management.entity.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    public static Specification<Employee> hasName(String name) {
        return (root, query, cb) ->
                (name == null || name.isEmpty()) ? null : cb.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Employee> hasDepartment(String department) {
        return (root, query, cb) ->
                (department == null || department.isEmpty()) ? null : cb.equal(root.get("department"), department);
    }
}
